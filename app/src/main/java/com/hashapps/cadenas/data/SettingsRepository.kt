package com.hashapps.cadenas.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
    private val modelsDir: File,
    private val profileDao: ProfileDao,
    externalScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private companion object {
        val SELECTED_PROFILE = intPreferencesKey("selected_profile")
        val RUN_ONCE = booleanPreferencesKey("run_once")

        const val TAG = "SettingsRepo"
    }

    val runOnce = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading settings.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { it[RUN_ONCE] ?: false }

    suspend fun completeFirstRun() {
        dataStore.edit {
            it[RUN_ONCE] = true
        }
    }

    suspend fun saveSelectedProfile(selectedProfile: Int) {
        dataStore.edit {
            it[SELECTED_PROFILE] = selectedProfile
        }
    }

    private var _cadenasInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val cadenasInitialized: StateFlow<Boolean> = _cadenasInitialized.asStateFlow()

    private var _selectedProfile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val selectedProfile: StateFlow<Profile?> = _selectedProfile.asStateFlow()

    init {
        externalScope.launch(ioDispatcher) {
            dataStore.data
                .catch {
                    if (it is IOException) {
                        Log.e(TAG, "Error reading settings.", it)
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }.map { preferences ->
                    preferences[SELECTED_PROFILE]
                }.filterNotNull().collectLatest {
                    profileDao.getProfile(it).collectLatest { profile ->
                        _selectedProfile.update { profile }

                        Cadenas.initialize(
                            CadenasConfig(
                                modelDir = modelsDir.resolve(profile.selectedModel).path,
                                key = profile.key,
                                seed = profile.seed,
                            )
                        )

                        if (Cadenas.getInstance() != null) {
                            _cadenasInitialized.update { true }
                        }
                    }
                }
        }
    }
}
package com.hashapps.cadenas.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.WorkManager
import com.hashapps.cadenas.data.profile.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

private const val CADENAS_SETTINGS_NAME = "cadenas_settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CADENAS_SETTINGS_NAME
)

interface AppContainer {
    val profileRepository: ProfileRepository
    val settingsRepository: SettingsRepository
    val modelRepository: ModelRepository
}

class AppDataContainer(
    private val context: Context
) : AppContainer {
    override val profileRepository by lazy {
        ProfileRepository(
            profileDao = ProfileDatabase.getDatabase(context).profileDao(),
        )
    }

    override val settingsRepository by lazy {
        SettingsRepository(
            dataStore = context.dataStore,
            internalStorage = context.filesDir,
            profileDao = ProfileDatabase.getDatabase(context).profileDao(),
            externalScope = CoroutineScope(SupervisorJob()),
        )
    }

    override val modelRepository by lazy {
        ModelRepository(
            internalStorage = context.filesDir,
            workManager = WorkManager.getInstance(context),
        )
    }
}
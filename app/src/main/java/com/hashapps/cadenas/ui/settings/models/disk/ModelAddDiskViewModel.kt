package com.hashapps.cadenas.ui.settings.models.disk

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashapps.cadenas.data.models.ModelRepository
import com.hashapps.cadenas.ui.settings.models.ModelUiState
import com.hashapps.cadenas.ui.settings.models.isValid
import com.hashapps.cadenas.ui.settings.models.toModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ModelAddDiskViewModel(
    private val modelRepository: ModelRepository,
) : ViewModel() {
    var modelUiState: ModelUiState by mutableStateOf(ModelUiState(onDisk = true))
        private set

    val modelNames = modelRepository.getAllModelsStream().map { model ->
        model.map { it.name }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = listOf(),
    )

    /**
     * Update the model-add screen UI state, only enabling the install button
     * if the new state is valid.
     */
    fun updateUiState(newModelUiState: ModelUiState) {
        modelUiState =
            newModelUiState.copy(actionEnabled = newModelUiState.isValid(modelNames.value))
    }

    val modelInstallerState = modelRepository.modelInstallerState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = null,
    )

    /**
     * If a valid name has been entered and a valid ZIP selected, start the
     * model-installation worker.
     */
    fun installModel() {
        if (modelUiState.isValid(modelNames.value)) {
            modelRepository.installModelFromAndSaveAs(
                Uri.parse(modelUiState.file),
                modelUiState.name
            )
        }
    }

    /**
     * Save an installed model to the database.
     */
    fun saveModel(hash: String) {
        viewModelScope.launch {
            modelRepository.insertModel(modelUiState.toModel(hash))
        }
    }
}
package com.hashapps.cadenas.ui.settings.models.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashapps.cadenas.data.SettingsRepository
import com.hashapps.cadenas.domain.ManageModelsUseCase
import kotlinx.coroutines.launch

/**
 * View model for the model-management screen.
 *
 * @property[selectedChannel] The currently-selected messaging channel
 * @property[availableModels] The list of all downloaded models
 */
class ManageModelsViewModel(
    private val manageModelsUseCase: ManageModelsUseCase,
    settingsRepository: SettingsRepository,
) : ViewModel() {
    val selectedChannel = settingsRepository.selectedChannel

    var availableModels: List<String> by mutableStateOf(manageModelsUseCase())
        private set

    /**
     * Start the model-deleting worker for the given model name and update the
     * list of available models.
     *
     * @param[model] The name of the model to delete
     */
    fun deleteModel(model: String) {
        viewModelScope.launch {
            availableModels = manageModelsUseCase(model)
        }
    }
}
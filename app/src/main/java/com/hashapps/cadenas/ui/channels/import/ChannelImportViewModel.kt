package com.hashapps.cadenas.ui.channels.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashapps.cadenas.data.channels.Channel
import com.hashapps.cadenas.data.channels.ChannelRepository
import kotlinx.coroutines.launch

class ChannelImportViewModel(
    private val channelRepository: ChannelRepository,
) : ViewModel() {
    fun saveChannelAndGoToEdit(channel: Channel, navigateToEdit: (Long) -> Unit) {
        viewModelScope.launch {
            navigateToEdit(channelRepository.insertChannel(channel))
        }
    }
}
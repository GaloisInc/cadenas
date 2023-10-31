package com.hashapps.cadenas.ui.settings.channels.importing

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val CHANNEL_IMPORT_ROUTE = "channel_import"

fun NavGraphBuilder.channelImportScreen(
    onNavigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToChannelEdit: (Int) -> Unit,
) {
    composable(
        route = CHANNEL_IMPORT_ROUTE,
    ) {
        ChannelImportScreen(
            onNavigateBack = onNavigateBack,
            onNavigateUp = onNavigateUp,
            onNavigateToChannelEdit = onNavigateToChannelEdit,
        )
    }
}

fun NavController.navigateToChannelImport(navOptions: NavOptions? = null) {
    this.navigate(CHANNEL_IMPORT_ROUTE, navOptions)
}
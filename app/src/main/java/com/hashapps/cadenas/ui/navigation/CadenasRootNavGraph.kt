package com.hashapps.cadenas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hashapps.cadenas.ui.home.HOME_ROUTE
import com.hashapps.cadenas.ui.home.homeScreen
import com.hashapps.cadenas.ui.processing.navigateToProcessing
import com.hashapps.cadenas.ui.processing.processingScreen
import com.hashapps.cadenas.ui.channels.add.channelAddScreen
import com.hashapps.cadenas.ui.channels.add.navigateToChannelAdd
import com.hashapps.cadenas.ui.channels.edit.channelEditScreen
import com.hashapps.cadenas.ui.channels.edit.navigateToChannelEdit
import com.hashapps.cadenas.ui.channels.export.channelExportScreen
import com.hashapps.cadenas.ui.channels.export.navigateToChannelExport
import com.hashapps.cadenas.ui.channels.import.channelImportScreen
import com.hashapps.cadenas.ui.channels.import.navigateToChannelImport
import com.hashapps.cadenas.ui.home.navigateToHome
import com.hashapps.cadenas.ui.settings.models.add.navigateToModelAdd
import com.hashapps.cadenas.ui.settings.models.disk.navigateToModelAddDisk

/**
 * Top-level navigation host for Cadenas (post setup).
 */
@Composable
fun CadenasRootNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = modifier,
    ) {
        homeScreen(
            onNavigateToSettings = { navController.navigateToSettingsGraph() },
            onNavigateToNewChannel = { navController.navigateToChannelAdd() },
            onNavigateToImportChannel = { navController.navigateToChannelImport() },
            onNavigateToChannel = { id, toDecode -> navController.navigateToProcessing(id, toDecode) },
            onNavigateToExportChannel = { navController.navigateToChannelExport(it) },
            onNavigateToEditChannel = { navController.navigateToChannelEdit(it) },
        )
        channelAddScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAddModel = { navController.navigateToModelAdd(it) },
            onNavigateToAddModelDisk = { navController.navigateToModelAddDisk() })
        channelImportScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToChannelEdit = { navController.navigateToChannelEdit(it) },
            onNavigateToAddModel = { navController.navigateToModelAdd(it) })
        channelExportScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateBackAfterSave = { navController.navigateToHome(true) }
        )
        channelEditScreen(onNavigateBack = { navController.popBackStack() })
        processingScreen { navController.navigateToHome(false) }
        settingsGraph(navController)
    }
}
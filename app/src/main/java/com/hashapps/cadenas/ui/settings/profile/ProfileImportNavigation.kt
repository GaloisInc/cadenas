package com.hashapps.cadenas.ui.settings.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val PROFILE_IMPORT_ROUTE = "profile_import"

fun NavGraphBuilder.profileImportScreen(
    onNavigateUp: () -> Unit,
    onNavigateProfileEdit: (Int) -> Unit,
) {
    composable(
        route = PROFILE_IMPORT_ROUTE,
    ) {
        ProfileImportScreen(
            onNavigateUp = onNavigateUp,
            onNavigateProfileEdit = onNavigateProfileEdit,
        )
    }
}

fun NavController.navigateToProfileImport(navOptions: NavOptions? = null) {
    this.navigate(PROFILE_IMPORT_ROUTE, navOptions)
}
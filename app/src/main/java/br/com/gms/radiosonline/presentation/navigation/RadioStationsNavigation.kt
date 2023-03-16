package br.com.gms.radiosonline.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

internal const val radioStationsRoute = "radio-stations"

fun NavController.navigateToRadioStations(navOptions: NavOptions? = null) {
    navigate(radioStationsRoute, navOptions)
}

fun NavGraphBuilder.radioStationsScreen(navController: NavController) {
    composable(radioStationsRoute) {
        //TODO - Implements
    }
}
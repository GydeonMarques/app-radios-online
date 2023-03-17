package br.com.gms.radiosonline.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsScreen
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsViewModel

internal const val radioStationsRoute = "radio-stations"

fun NavController.navigateToRadioStations(navOptions: NavOptions? = null) {
    navigate(radioStationsRoute, navOptions)
}

fun NavGraphBuilder.radioStationsScreen(navController: NavController) {
    composable(radioStationsRoute) {
        val viewModel: RadioStationsViewModel = hiltViewModel()
        RadioStationsScreen(
            viewModel = viewModel,
            onRadioNavigationToPlayer = { radioId ->
                navController.navigateToPlayingNow(radioId = radioId)
            },
        )
    }
}
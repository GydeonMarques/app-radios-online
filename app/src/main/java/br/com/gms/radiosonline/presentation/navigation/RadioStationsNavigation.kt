package br.com.gms.radiosonline.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsTabScreen
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsViewModelFactory

internal const val radioStationsRoute = "radio-stations"

fun NavController.navigateToRadioStations(navOptions: NavOptions? = null) {
    navigate(radioStationsRoute, navOptions)
}

fun NavGraphBuilder.radioStationsScreen(
    navController: NavController,
    viewModelFactory: RadioStationsViewModelFactory
) {
    composable(radioStationsRoute) {
        RadioStationsTabScreen(
            viewModel = hiltViewModel(),
            viewModelFactory = viewModelFactory,
            onRadioNavigationToPlayer = { radioId ->
                navController.navigateToPlayingNow(radioId = radioId)
            },
        )
    }
}
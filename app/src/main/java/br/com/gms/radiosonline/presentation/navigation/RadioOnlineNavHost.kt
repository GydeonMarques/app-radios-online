package br.com.gms.radiosonline.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsViewModelFactory

@Composable
fun RadioOnlineNavHost(
    navController: NavHostController,
    viewModelFactory: RadioStationsViewModelFactory
) {

    NavHost(
        navController = navController,
        startDestination = radioStationsRoute
    ) {
        radioStationsScreen(navController = navController, viewModelFactory = viewModelFactory)
        playingNowScreen(navController = navController)
        favoritesScreen(navController = navController)
    }
}
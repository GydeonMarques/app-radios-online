package br.com.gms.webradios.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.gms.webradios.presentation.screens.radio_stations.RadioStationsViewModelFactory

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
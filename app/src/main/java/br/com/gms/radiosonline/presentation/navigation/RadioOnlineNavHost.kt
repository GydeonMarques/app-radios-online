package br.com.gms.radiosonline.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RadioOnlineNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = radioStationsRoute
    ) {
        radioStationsScreen(navController = navController)
        playingNowScreen(navController = navController)
        favoritesScreen(navController = navController)
    }
}
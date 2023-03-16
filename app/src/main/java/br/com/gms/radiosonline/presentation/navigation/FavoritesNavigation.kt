package br.com.gms.radiosonline.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


internal const val favoritesRoute = "favorites"

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
    navigate(favoritesRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen(navController: NavController) {
    composable(favoritesRoute) {
        //TODO - Implements
    }
}
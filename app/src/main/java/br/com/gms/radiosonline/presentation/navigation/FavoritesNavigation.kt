package br.com.gms.radiosonline.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.gms.radiosonline.presentation.screens.favorites.FavoritesRadiosScreen
import br.com.gms.radiosonline.presentation.screens.favorites.FavoritesViewModel


internal const val favoritesRoute = "favorites"

fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
    navigate(favoritesRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen(navController: NavController) {
    composable(favoritesRoute) {
        val viewModel: FavoritesViewModel = hiltViewModel()
        FavoritesRadiosScreen(
            viewModel = viewModel,
            onRadioNavigationToPlayer = { radioId ->
                navController.navigateToPlayingNow(radioId)
            }
        )
    }
}
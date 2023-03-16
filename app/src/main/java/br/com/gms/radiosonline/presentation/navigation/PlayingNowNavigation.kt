package br.com.gms.radiosonline.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val radioIdParam = "radioId"
internal const val playingNowRoute = "playing-now"

fun NavController.navigateToPlayingNow(navOptions: NavOptions? = null) {
    navigate(playingNowRoute, navOptions)
}

fun NavController.navigateToPlayingNow(radioId: String, navOptions: NavOptions? = null) {
    navigate("$playingNowRoute?$radioIdParam=$radioId", navOptions)
}

fun NavGraphBuilder.playingNowScreen(navController: NavController) {
    composable("$playingNowRoute?$radioIdParam={$radioIdParam}") {
        //TODO - Implements
    }
}
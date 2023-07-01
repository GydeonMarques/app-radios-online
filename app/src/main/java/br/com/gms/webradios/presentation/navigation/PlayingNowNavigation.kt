package br.com.gms.webradios.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.gms.webradios.presentation.screens.playing_now.PlayingNowContent
import br.com.gms.webradios.presentation.screens.playing_now.PlayingNowViewModel

internal const val radioIdParam = "radioId"
internal const val playingNowRoute = "playing-now"

fun NavController.navigateToPlayingNow(navOptions: NavOptions? = null) {
    navigate(playingNowRoute, navOptions)
}

fun NavController.navigateToPlayingNow(radioId: String, navOptions: NavOptions? = null) {
    navigate("$playingNowRoute?$radioIdParam=$radioId", navOptions)
}

fun NavGraphBuilder.playingNowScreen(navController: NavController) {
    composable("$playingNowRoute?$radioIdParam={$radioIdParam}") {
        val viewModel: PlayingNowViewModel = hiltViewModel()
        PlayingNowContent(
            viewModel = viewModel
        )
    }
}
package br.com.gms.webradios.presentation.screens.playing_now

import br.com.gms.webradios.domain.model.RadioModel

sealed class PlayingNowUiState {
    object Loading : PlayingNowUiState()
    object Nothing : PlayingNowUiState()
    data class Failure(val error: Throwable?) : PlayingNowUiState()
    data class Success(val radioStation: RadioModel? = null) : PlayingNowUiState()
}

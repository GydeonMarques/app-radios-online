package br.com.gms.webradios.presentation.screens.favorites

import br.com.gms.webradios.domain.model.RadioModel

sealed class RadioStationsFavoritesUiState {
    object Loading : RadioStationsFavoritesUiState()
    data class Failure(val error: Throwable?) : RadioStationsFavoritesUiState()
    data class Success(val radios: Map<String, List<RadioModel>> = emptyMap()) : RadioStationsFavoritesUiState()
}

package br.com.gms.radiosonline.presentation.screens.favorites

import br.com.gms.radiosonline.domain.model.RadioModel

sealed class RadioStationsFavoritesUiState {
    object Loading : RadioStationsFavoritesUiState()
    data class Failure(val error: Throwable?) : RadioStationsFavoritesUiState()
    data class Success(val radios: Map<String, List<RadioModel>> = emptyMap()) : RadioStationsFavoritesUiState()
}

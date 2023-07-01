package br.com.gms.webradios.presentation.screens.radio_stations

import br.com.gms.webradios.domain.model.RadioModel

sealed class RadioStationsUiState {
    object Loading : RadioStationsUiState()
    data class Failure(val error: Throwable?) : RadioStationsUiState()
    data class Success(
        val topRadiosStations: List<RadioModel> = emptyList(),
        val listRadioStations: Map<String, List<RadioModel>> = emptyMap()
    ) : RadioStationsUiState()
}

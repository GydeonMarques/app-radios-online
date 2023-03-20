package br.com.gms.radiosonline.presentation.screens.radio_stations

import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel

sealed class RadioStationsUiState {
    object Loading : RadioStationsUiState()
    data class Failure(val error: Throwable?) : RadioStationsUiState()
    data class Success(
        val categories: List<RadioCategoryModel> = emptyList(),
        val topRadiosStations: List<RadioModel> = emptyList(),
        val listRadioStations: Map<String, List<RadioModel>> = emptyMap()
    ) : RadioStationsUiState()
}

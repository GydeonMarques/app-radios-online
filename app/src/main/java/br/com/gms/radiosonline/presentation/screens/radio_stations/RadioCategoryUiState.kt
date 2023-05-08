package br.com.gms.radiosonline.presentation.screens.radio_stations

sealed class RadioCategoryUiState {
    object Loading : RadioCategoryUiState()
    data class Failure(val error: Throwable?) : RadioCategoryUiState()
    data class Success(val categories: List<String> = emptyList()) : RadioCategoryUiState()
}

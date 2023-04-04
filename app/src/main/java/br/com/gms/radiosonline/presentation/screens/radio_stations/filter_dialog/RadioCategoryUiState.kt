package br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog

import br.com.gms.radiosonline.domain.model.RadioCategoryModel

sealed class RadioCategoryUiState {
    object Loading : RadioCategoryUiState()
    data class Failure(val error: Throwable?) : RadioCategoryUiState()
    data class Success(val categories: List<RadioCategoryModel> = emptyList()) : RadioCategoryUiState()
}

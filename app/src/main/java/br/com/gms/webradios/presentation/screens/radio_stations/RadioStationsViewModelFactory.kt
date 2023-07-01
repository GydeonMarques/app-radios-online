package br.com.gms.webradios.presentation.screens.radio_stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.gms.webradios.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.webradios.domain.usecase.RadioStationsListUseCase
import javax.inject.Inject

class RadioStationsViewModelFactory @Inject constructor(
    private val useCase: RadioStationsListUseCase,
    private val favoriteUseCase: RadioStationsFavoritesUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioStationsViewModel::class.java)) {
            return RadioStationsViewModel(useCase, favoriteUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
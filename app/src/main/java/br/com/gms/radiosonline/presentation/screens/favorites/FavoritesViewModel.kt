package br.com.gms.radiosonline.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gms.radiosonline.data.model.mapper.mapListOfRadiosByCategories
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val useCase: RadioStationsFavoritesUseCase
) : ViewModel() {

    private var _radioStationsFavoritesUiState = MutableStateFlow<RadioStationsFavoritesUiState>(RadioStationsFavoritesUiState.Loading)
    val radioStationsFavoritesUiState: StateFlow<RadioStationsFavoritesUiState> get() = _radioStationsFavoritesUiState

    init {
        getFavoriteRadioStations()
    }

    private fun getFavoriteRadioStations() {
        viewModelScope.launch {
            _radioStationsFavoritesUiState.update { RadioStationsFavoritesUiState.Loading }
            useCase.getFavoriteRadioStations()
                .catch { RadioStationsFavoritesUiState.Failure(it) }
                .collect { radios ->
                    _radioStationsFavoritesUiState.update {
                        RadioStationsFavoritesUiState.Success(
                            mapListOfRadiosByCategories(radios)
                        )
                    }
                }
        }
    }

    fun searchFavoriteRadioStations(text: String) {
        viewModelScope.launch {
            useCase.searchFavoriteRadioStations(text)
                .catch { RadioStationsFavoritesUiState.Failure(it) }
                .collect { radios ->
                    _radioStationsFavoritesUiState.update {
                        RadioStationsFavoritesUiState.Success(
                            mapListOfRadiosByCategories(radios)
                        )
                    }
                }
        }
    }

    fun removeRadioStationFromFavorites(radioModel: RadioModel) {
        viewModelScope.launch {
            useCase.addOrRemoveRadioStationFromFavorites(radioModel)
        }
    }

}

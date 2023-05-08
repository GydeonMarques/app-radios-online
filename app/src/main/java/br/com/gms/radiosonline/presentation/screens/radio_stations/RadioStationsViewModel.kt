package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioStationsViewModel @Inject constructor(
    private val useCase: RadioStationsListUseCase,
    private val favoriteUseCase: RadioStationsFavoritesUseCase,
) : ViewModel() {

    private var category: String = ""
    private var searchTimeout = System.currentTimeMillis()

    private var _radioStationsUiState = MutableStateFlow<RadioStationsUiState>(RadioStationsUiState.Loading)
    val radioStationsUiState: StateFlow<RadioStationsUiState> get() = _radioStationsUiState

    private var _radioCategoriesUiState = MutableStateFlow<RadioCategoryUiState>(RadioCategoryUiState.Loading)
    val radioCategoriesUiState: StateFlow<RadioCategoryUiState> get() = _radioCategoriesUiState

    init {
        getRadioCategories()
    }

    fun getRadioStations(category: String) {
        viewModelScope.launch {
            this@RadioStationsViewModel.category = category
            _radioStationsUiState.update { RadioStationsUiState.Loading }
            useCase.getRadioStations(category).collect { response ->
                when (response) {
                    is ResultModel.Failure -> _radioStationsUiState.update {
                        RadioStationsUiState.Failure(response.throwable)
                    }
                    is ResultModel.Success -> _radioStationsUiState.update {
                        RadioStationsUiState.Success(
                            topRadiosStations = response.data.filter { it.topRadio },
                            listRadioStations = response.data.groupBy { it.category }
                        )
                    }
                }
            }
        }
    }

    private fun getRadioCategories() {
        viewModelScope.launch {
            useCase.getRadioCategories().collect { response ->
                when (response) {
                    is ResultModel.Failure -> _radioCategoriesUiState.update {
                        RadioCategoryUiState.Failure(
                            error = response.throwable
                        )
                    }
                    is ResultModel.Success -> _radioCategoriesUiState.update {
                        RadioCategoryUiState.Success(
                            categories = response.data.sortedBy { it.name }.map { it.name }
                        )
                    }
                }
            }
        }
    }

    fun searchRadioStations(text: String) {
        if ((System.currentTimeMillis() - searchTimeout) >= 500 || text.isEmpty()) {
            searchTimeout = System.currentTimeMillis()
            viewModelScope.launch {
                useCase.searchRadioStations(category, text).collect { response ->
                    when (response) {
                        is ResultModel.Failure -> _radioStationsUiState.update {
                            RadioStationsUiState.Failure(response.throwable)
                        }
                        is ResultModel.Success -> _radioStationsUiState.update {
                            RadioStationsUiState.Success(
                                topRadiosStations = response.data.filter { r -> r.topRadio },
                                listRadioStations = response.data.groupBy { it.category }
                            )
                        }
                    }
                }
            }
        } else return
    }

    fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel) {
        viewModelScope.launch {
            _radioStationsUiState.value.also { state ->
                if (state is RadioStationsUiState.Success) {

                    val hasBeenAddedOrRemovedSuccessfully = favoriteUseCase.addOrRemoveRadioStationFromFavorites(
                        radioModel.copy(
                            isFavorite = !radioModel.isFavorite
                        )
                    )

                    if (hasBeenAddedOrRemovedSuccessfully) {

                        mutableMapOf<String, MutableList<RadioModel>>().apply {

                            putAll(
                                state.listRadioStations.map { item ->
                                    Pair(item.key, item.value.map { it.copy() }.toMutableList())
                                }
                            )

                            forEach { item ->
                                if (item.key == radioModel.category) {
                                    item.value.forEach {
                                        if (it.id == radioModel.id) {
                                            it.isFavorite = !it.isFavorite
                                        }
                                    }
                                }
                            }

                        }.also { items ->
                            _radioStationsUiState.update { state.copy(listRadioStations = items) }
                        }
                    }
                }
            }
        }
    }
}

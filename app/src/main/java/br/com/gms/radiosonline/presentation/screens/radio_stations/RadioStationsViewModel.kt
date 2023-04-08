package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gms.radiosonline.data.model.mapper.mapListOfRadiosByCategories
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCase
import br.com.gms.radiosonline.presentation.screens.favorites.RadioStationsFavoritesUiState
import br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog.RadioCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RadioStationsViewModel @Inject constructor(
    private val useCase: RadioStationsListUseCase,
    private val favoriteUseCase: RadioStationsFavoritesUseCase,
) : ViewModel() {

    private var _radioStationsUiState = MutableStateFlow<RadioStationsUiState>(RadioStationsUiState.Loading)
    val radioStationsUiState: StateFlow<RadioStationsUiState> get() = _radioStationsUiState

    private var _radioCategoriesUiState = MutableStateFlow<RadioCategoryUiState>(RadioCategoryUiState.Loading)
    val radioCategoriesUiState: StateFlow<RadioCategoryUiState> get() = _radioCategoriesUiState

    private var _appliedFilter = MutableStateFlow(false)
    val appliedFilter: StateFlow<Boolean> get() = _appliedFilter

    private var searchTimeout = System.currentTimeMillis()

    init {
        getRadioStations()
        getRadioCategories()
    }

    private fun getRadioStations() {
        viewModelScope.launch {
            _radioStationsUiState.update { RadioStationsUiState.Loading }
            useCase.getRadioStations()
                .collect { response ->
                    when (response) {
                        is ResultModel.Failure -> _radioStationsUiState.update {
                            RadioStationsUiState.Failure(response.throwable)
                        }
                        is ResultModel.Success -> _radioStationsUiState.update {
                            RadioStationsUiState.Success(
                                topRadiosStations = response.data.filter { it.topRadio },
                                listRadioStations = mapListOfRadiosByCategories(response.data)
                            )
                        }
                    }
                }
        }
    }

    private fun getRadioCategories() {
        viewModelScope.launch {
            useCase.getRadioCategories()
                .collect { response ->
                    when (response) {
                        is ResultModel.Failure -> _radioCategoriesUiState.update {
                            RadioCategoryUiState.Failure(
                                error = response.throwable
                            )
                        }
                        is ResultModel.Success -> _radioCategoriesUiState.update {
                            RadioCategoryUiState.Success(
                                categories = response.data
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
                useCase.searchRadioStations(text)
                    .collect { response ->
                        when (response) {
                            is ResultModel.Failure -> _radioStationsUiState.update {
                                RadioStationsUiState.Failure(response.throwable)
                            }
                            is ResultModel.Success -> _radioStationsUiState.update {
                                RadioStationsUiState.Success(
                                    topRadiosStations = response.data.filter { r -> r.topRadio },
                                    listRadioStations = mapListOfRadiosByCategories(response.data)
                                )
                            }
                        }
                    }
            }
        } else return
    }

    fun applyRadioStationFilter() {
        radioCategoriesUiState.value.takeIf { it is RadioCategoryUiState.Success }?.let { it ->
            val categories = (it as RadioCategoryUiState.Success).categories
                .filter { it.selected }
                .map { it.name }

            if (categories.isEmpty()) {
                getRadioStations()
                _appliedFilter.update { false }
            } else {
                viewModelScope.launch {
                    _appliedFilter.update { true }
                    _radioStationsUiState.update { RadioStationsUiState.Loading }
                    useCase.getRadioStationsByCategory(categories)
                        .collect { response ->
                            when (response) {
                                is ResultModel.Failure -> _radioStationsUiState.update {
                                    RadioStationsUiState.Failure(response.throwable)
                                }
                                is ResultModel.Success -> _radioStationsUiState.update {
                                    RadioStationsUiState.Success(
                                        topRadiosStations = response.data.filter { it.topRadio },
                                        listRadioStations = mapListOfRadiosByCategories(response.data)
                                    )
                                }
                            }
                        }
                }
            }
        }
    }

    fun onCategorySelected(category: RadioCategoryModel) {
        val categoriesUiState = _radioCategoriesUiState.value

        if (categoriesUiState is RadioCategoryUiState.Success) {

            val items = categoriesUiState.categories.map {
                it.copy(selected = if (it.id == category.id) !it.selected else it.selected)
            }

            _radioCategoriesUiState.update {
                categoriesUiState.copy(categories = items)
            }
        }
    }

    fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel) {
        viewModelScope.launch {
            if (_radioStationsUiState.value is RadioStationsUiState.Success) {

                val state = (_radioStationsUiState.value as RadioStationsUiState.Success)

                val hasBeenAddedOrRemovedSuccessfully = favoriteUseCase.addOrRemoveRadioStationFromFavorites(radioModel.copy(
                    isFavorite = !radioModel.isFavorite
                ))

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

                        _radioStationsUiState.value = state.copy(listRadioStations = this)
                    }
                }
            }
        }
    }
}

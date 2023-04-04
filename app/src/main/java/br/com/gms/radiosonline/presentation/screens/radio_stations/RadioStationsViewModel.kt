package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usercase.RadioStationsUserCase
import br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog.RadioCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RadioStationsViewModel @Inject constructor(
    private val useCase: RadioStationsUserCase,
) : ViewModel() {

    private val _radioStationsUiStateBkp = MutableStateFlow<RadioStationsUiState>(RadioStationsUiState.Loading)

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
                            ).also {
                                _radioStationsUiStateBkp.value = it
                            }
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
        if (text.isBlank()) {
            _radioStationsUiState.update {
               _radioStationsUiStateBkp.value
            }
        } else {
            if ((System.currentTimeMillis() - searchTimeout) >= 1000) {
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

    private suspend fun mapListOfRadiosByCategories(list: List<RadioModel>): MutableMap<String, ArrayList<RadioModel>> {
        return withContext(Dispatchers.Default) {
            mutableMapOf<String, ArrayList<RadioModel>>().also { items ->
                list.forEach { radioModel ->
                    if (items.containsKey(radioModel.category)) {
                        if (items[radioModel.category]?.contains(radioModel) == false) {
                            items[radioModel.category]?.add(radioModel)
                        }
                    } else {
                        items[radioModel.category] = arrayListOf(radioModel)
                    }
                }
            }
        }
    }
}

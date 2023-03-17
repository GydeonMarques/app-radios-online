package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usercase.RadioStationsUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RadioStationsViewModel @Inject constructor(
    private val useCase: RadioStationsUserCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow<RadioStationsUiState>(RadioStationsUiState.Loading)
    val uiState: StateFlow<RadioStationsUiState> get() = _uiState

    init {
        getRadioStations()
    }


    private fun getRadioStations() {
        viewModelScope.launch {
            _uiState.update { RadioStationsUiState.Loading }
            useCase.getRadioStations()
                .collect { response ->
                    when (response) {
                        is ResultModel.Failure -> _uiState.update {
                            RadioStationsUiState.Failure(response.throwable)
                        }
                        is ResultModel.Success -> _uiState.update {
                            RadioStationsUiState.Success(
                                topRadiosStations = response.data.filter { it.topRadio },
                                listRadioStations = mapListOfRadiosByCategories(response.data)
                            )
                        }
                    }
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

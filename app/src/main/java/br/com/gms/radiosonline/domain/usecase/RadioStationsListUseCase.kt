package br.com.gms.radiosonline.domain.usecase

import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsListUseCase {
    suspend fun getRadioStations(category: String): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioStationById(id: String): Flow<ResultModel<RadioModel?>>
    suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryModel>>>
    suspend fun searchRadioStations(category: String, text: String): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioModel>>>
}


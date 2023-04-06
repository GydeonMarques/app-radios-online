package br.com.gms.radiosonline.domain.usercase

import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsUserCase {
    suspend fun getRadioStationById(id: String): Flow<ResultModel<RadioModel?>>
    suspend fun getRadioStations(): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryModel>>>
    suspend fun searchRadioStations(text: String): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioModel>>>
}

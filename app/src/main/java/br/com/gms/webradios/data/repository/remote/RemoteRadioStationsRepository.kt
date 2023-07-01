package br.com.gms.webradios.data.repository.remote

import br.com.gms.webradios.data.model.remote.ResultModel
import br.com.gms.webradios.domain.model.RadioCategoryModel
import br.com.gms.webradios.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow

interface RemoteRadioStationsRepository {
    suspend fun getRadioStations(category: String): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioStationById(id: String): Flow<ResultModel<RadioModel?>>
    suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryModel>>>
    suspend fun searchRadioStations(category: String, text: String): Flow<ResultModel<List<RadioModel>>>
    suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioModel>>>
}

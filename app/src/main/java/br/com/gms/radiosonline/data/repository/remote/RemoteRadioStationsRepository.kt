package br.com.gms.radiosonline.data.repository.remote

import br.com.gms.radiosonline.data.model.RadioCategoryResponseModel
import br.com.gms.radiosonline.data.model.RadioResponseModel
import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import kotlinx.coroutines.flow.Flow

interface RemoteRadioStationsRepository {
    suspend fun getRadioStations(): Flow<ResultModel<List<RadioResponseModel>>>
    suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryResponseModel>>>
}
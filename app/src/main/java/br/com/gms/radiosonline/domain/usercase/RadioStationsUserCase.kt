package br.com.gms.radiosonline.domain.usercase

import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsUserCase {
    suspend fun getRadioStations(): Flow<ResultModel<List<RadioModel>>>
}

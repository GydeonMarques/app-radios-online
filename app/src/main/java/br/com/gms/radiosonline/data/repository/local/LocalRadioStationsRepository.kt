package br.com.gms.radiosonline.data.repository.local

import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface LocalRadioStationsRepository {
    suspend fun getRadioStationFavorites(): Flow<List<RadioModel>>
    suspend fun getRadioStationFavoriteById(id: String): RadioModel?
    suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel)
    suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>>
}

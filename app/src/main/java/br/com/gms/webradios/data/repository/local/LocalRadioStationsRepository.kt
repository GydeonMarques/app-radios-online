package br.com.gms.webradios.data.repository.local

import br.com.gms.webradios.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface LocalRadioStationsRepository {
    suspend fun getRadioStationFavorites(): Flow<List<RadioModel>>
    suspend fun getRadioStationFavoriteById(id: String): RadioModel?
    suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>>
    suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel): Boolean
}

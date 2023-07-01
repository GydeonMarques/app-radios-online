package br.com.gms.webradios.domain.usecase

import br.com.gms.webradios.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsFavoritesUseCase {
    suspend fun getFavoriteRadioStations(): Flow<List<RadioModel>>
    suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>>
    suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel): Boolean
}

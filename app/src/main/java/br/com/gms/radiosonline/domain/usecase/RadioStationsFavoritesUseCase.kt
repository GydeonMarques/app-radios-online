package br.com.gms.radiosonline.domain.usecase

import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsFavoritesUseCase {
    suspend fun getFavoriteRadioStations(): Flow<List<RadioModel>>
    suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>>
    suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel): Boolean
}

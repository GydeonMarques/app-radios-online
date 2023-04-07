package br.com.gms.radiosonline.domain.usecase

import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow


interface RadioStationsFavoritesUseCase {
    suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel)
    suspend fun getFavoriteRadioStations(): Flow<Map<String, List<RadioModel>>>
    suspend fun searchFavoriteRadioStations(text: String): Flow<Map<String, List<RadioModel>>>
}

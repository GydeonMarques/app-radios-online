package br.com.gms.webradios.domain.usecase

import br.com.gms.webradios.data.repository.local.LocalRadioStationsRepository
import br.com.gms.webradios.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RadioStationsFavoritesUseCaseImpl @Inject constructor(
    private val localRadioStationsRepository: LocalRadioStationsRepository,
) : RadioStationsFavoritesUseCase {

    override suspend fun getFavoriteRadioStations(): Flow<List<RadioModel>> {
        return localRadioStationsRepository.getRadioStationFavorites()
    }

    override suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>> {
        return localRadioStationsRepository.searchFavoriteRadioStations(text)
    }

    override suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel): Boolean {
       return localRadioStationsRepository.addOrRemoveRadioStationFromFavorites(radioModel)
    }

}

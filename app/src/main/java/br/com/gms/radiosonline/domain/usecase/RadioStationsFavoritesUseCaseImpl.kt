package br.com.gms.radiosonline.domain.usecase

import br.com.gms.radiosonline.data.repository.local.LocalRadioStationsRepository
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.radiosonline.di.IoDispatcher
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.CoroutineDispatcher
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

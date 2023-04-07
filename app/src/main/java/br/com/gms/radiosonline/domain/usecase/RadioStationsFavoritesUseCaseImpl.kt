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
    private val remoteRadioStationsRepository: RemoteRadioStationsRepository,
) : RadioStationsFavoritesUseCase {

    override suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteRadioStations(): Flow<Map<String, List<RadioModel>>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchFavoriteRadioStations(text: String): Flow<Map<String, List<RadioModel>>> {
        TODO("Not yet implemented")
    }
}

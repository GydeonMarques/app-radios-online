package br.com.gms.radiosonline.domain.usercase

import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.data.model.toModel
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.radiosonline.di.IoDispatcher
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RadioStationsUserCaseImpl @Inject constructor(
    private val remoteRadioStationsRepository: RemoteRadioStationsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RadioStationsUserCase {

    override suspend fun getRadioStations(): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioStations()
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(
                            result.data.map { it.toModel() }
                        )
                    }
                }
        }
    }
}

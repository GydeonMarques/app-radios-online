package br.com.gms.radiosonline.domain.usecase

import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.radiosonline.di.IoDispatcher
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RadioStationsListUseCaseImpl @Inject constructor(
    private val remoteRadioStationsRepository: RemoteRadioStationsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RadioStationsListUseCase {

    override suspend fun getRadioStationById(id: String): Flow<ResultModel<RadioModel?>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioStationById(id)
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }


    override suspend fun getRadioStations(): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioStations()
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }

    override suspend fun searchRadioStations(text: String): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.searchRadioStations(text)
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }

    override suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioStationsByCategory(categories)
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }

    override suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioCategories()
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }
}

package br.com.gms.webradios.domain.usecase

import br.com.gms.webradios.data.model.remote.ResultModel
import br.com.gms.webradios.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.webradios.di.IoDispatcher
import br.com.gms.webradios.domain.model.RadioCategoryModel
import br.com.gms.webradios.domain.model.RadioModel
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


    override suspend fun getRadioStations(category: String): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.getRadioStations(category)
                .map { result ->
                    when (result) {
                        is ResultModel.Failure -> result
                        is ResultModel.Success -> ResultModel.Success(result.data)
                    }
                }
        }
    }

    override suspend fun searchRadioStations(category: String, text: String): Flow<ResultModel<List<RadioModel>>> {
        return withContext(dispatcher) {
            remoteRadioStationsRepository.searchRadioStations(category, text)
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

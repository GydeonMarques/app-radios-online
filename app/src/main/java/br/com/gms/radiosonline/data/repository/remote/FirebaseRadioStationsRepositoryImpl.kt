package br.com.gms.radiosonline.data.repository.remote

import br.com.gms.radiosonline.data.model.mapper.toModel
import br.com.gms.radiosonline.data.model.mapper.toRadioCategoryModel
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.data.repository.local.LocalRadioStationsRepository
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val FILTER_FIELD_NAME = "category"
private const val RADIO_STATIONS_COLLECTION_NAME = "radio_stations"
private const val RADIO_CATEGORIES_COLLECTION_NAME = "radio_categories"

class FirebaseRadioStationsRepositoryImpl @Inject constructor(
    private val localRadioStationsRepository: LocalRadioStationsRepository
) : RemoteRadioStationsRepository {

    override suspend fun getRadioStationById(id: String): Flow<ResultModel<RadioModel?>> {
        return callbackFlow {
            try {
                val radioFavorite = localRadioStationsRepository.getRadioStationFavoriteById(id)
                Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME).document(id).also {
                    val subscription = it.addSnapshotListener { snapshot, error ->
                        snapshot?.let { doc ->
                            trySend(
                                ResultModel.Success(
                                    doc.toModel().copy(
                                        isFavorite = radioFavorite?.isFavorite == true
                                    )
                                )
                            )

                        } ?: error?.let {
                            trySend(ResultModel.Failure(error))
                        }
                    }

                    awaitClose { subscription.remove() }
                }
            } catch (e: Throwable) {
                trySend(ResultModel.Failure(e))
                close(e)
            }
        }
    }

    override suspend fun getRadioStations(category: String): Flow<ResultModel<List<RadioModel>>> {
        return callbackFlow {
            try {

                var query: Query? = null
                val collection = Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME)

                if (category.isNotEmpty()) {
                    query = collection.whereIn(FILTER_FIELD_NAME, listOf(category))
                }

                val subscription = (query ?: collection).addSnapshotListener { snapshot, error ->
                    snapshot?.let {
                        snapshot.documents
                            .map { doc -> doc.toModel() }
                            .apply {
                                trySend(ResultModel.Success(this))
                            }
                    } ?: error?.let {
                        trySend(ResultModel.Failure(error))
                    }
                }

                awaitClose { subscription.remove() }

            } catch (e: Throwable) {
                trySend(ResultModel.Failure(e))
                close(e)
            }
        }.checkFavoriteItems()
    }

    override suspend fun searchRadioStations(
        category: String,
        text: String
    ): Flow<ResultModel<List<RadioModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME)
                    .whereIn(FILTER_FIELD_NAME, listOf(category))
                    .also {
                        val subscription = it.addSnapshotListener { snapshot, error ->
                            snapshot?.let {
                                snapshot.documents
                                    .map { doc -> doc.toModel() }
                                    .filter { doc ->
                                        doc.name.contains(
                                            text,
                                            ignoreCase = true
                                        )
                                    }
                                    .apply {
                                        trySend(ResultModel.Success(this))
                                    }
                            } ?: error?.let {
                                trySend(ResultModel.Failure(error))
                            }
                        }

                        awaitClose { subscription.remove() }
                    }

            } catch (e: Throwable) {
                trySend(ResultModel.Failure(e))
                close(e)
            }
        }.checkFavoriteItems()
    }

    override suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME)
                    .whereIn(FILTER_FIELD_NAME, categories)
                    .also {
                        val subscription = it.addSnapshotListener { snapshot, error ->
                            snapshot?.let {
                                snapshot.documents
                                    .map { doc -> doc.toModel() }
                                    .apply {
                                        trySend(ResultModel.Success(this))
                                    }
                            } ?: error?.let {
                                trySend(ResultModel.Failure(error))
                            }
                        }

                        awaitClose { subscription.remove() }
                    }
            } catch (e: Throwable) {
                trySend(ResultModel.Failure(e))
                close(e)
            }
        }.checkFavoriteItems()
    }

    override suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_CATEGORIES_COLLECTION_NAME).also {
                    val subscription = it.addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            snapshot.documents.map { doc -> doc.toRadioCategoryModel() }.apply {
                                trySend(ResultModel.Success(this))
                            }
                        } ?: error?.let {
                            trySend(ResultModel.Failure(error))
                        }
                    }

                    awaitClose { subscription.remove() }
                }
            } catch (e: Throwable) {
                trySend(ResultModel.Failure(e))
                close(e)
            }
        }
    }

    private suspend fun Flow<ResultModel<List<RadioModel>>>.checkFavoriteItems(): Flow<ResultModel<List<RadioModel>>> {
        return combine(localRadioStationsRepository.getRadioStationFavorites()) { result, favorites ->
            when (result) {
                is ResultModel.Success -> result.copy(
                    data = result.data.map { radioModel ->
                        radioModel.copy(
                            isFavorite = favorites.find { favorite ->
                                favorite.id == radioModel.id
                            } != null
                        )
                    }
                )
                else -> result
            }
        }
    }
}
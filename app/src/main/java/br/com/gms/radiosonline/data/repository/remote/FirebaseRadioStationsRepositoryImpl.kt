package br.com.gms.radiosonline.data.repository.remote

import br.com.gms.radiosonline.data.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val FILTER_FIELD_NAME = "category"
private const val RADIO_STATIONS_COLLECTION_NAME = "radio_stations"
private const val RADIO_CATEGORIES_COLLECTION_NAME = "radio_categories"

class FirebaseRadioStationsRepositoryImpl : RemoteRadioStationsRepository {

    override suspend fun getRadioStations(): Flow<ResultModel<List<RadioResponseModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME).also {
                    val subscription = it.addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            snapshot.documents.map { doc -> doc.toModel() }.apply {
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

    override suspend fun getRadioStationsByCategory(categories: List<String>): Flow<ResultModel<List<RadioResponseModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_STATIONS_COLLECTION_NAME)
                    .whereIn(FILTER_FIELD_NAME, categories)
                    .also {
                        val subscription = it.addSnapshotListener { snapshot, error ->
                            snapshot?.let {
                                snapshot.documents.map { doc -> doc.toModel() }.apply {
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

    override suspend fun getRadioCategories(): Flow<ResultModel<List<RadioCategoryResponseModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(RADIO_CATEGORIES_COLLECTION_NAME).also {
                    val subscription = it.addSnapshotListener { snapshot, error ->
                        snapshot?.let {
                            snapshot.documents.map { doc -> doc.toRadioCategoryResponse() }.apply {
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
}
package br.com.gms.radiosonline.data.repository.remote

import br.com.gms.radiosonline.data.model.RadioResponseModel
import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.data.model.toModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val COLLECTION_NAME = "radio_stations"

class FirebaseRadioStationsRepositoryImpl : RemoteRadioStationsRepository {

    override suspend fun getRadioStations(): Flow<ResultModel<List<RadioResponseModel>>> {
        return callbackFlow {
            try {
                Firebase.firestore.collection(COLLECTION_NAME).also {
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
}
package br.com.gms.radiosonline.data.repository.local

import br.com.gms.radiosonline.data.database.RadioStationsDatabase
import br.com.gms.radiosonline.data.model.mapper.toEntity
import br.com.gms.radiosonline.data.model.mapper.toModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LocalRadioStationsRepositoryImpl @Inject constructor(
    database: RadioStationsDatabase
) : LocalRadioStationsRepository {

    private val db = database.radioStationsFavoritesDAO

    override suspend fun getRadioStationFavorites(): Flow<List<RadioModel>> {
        return db.getAll().map { it.toModel() }
    }

    override suspend fun getRadioStationFavoriteById(id: String): RadioModel? {
        return db.getById(id)?.toModel()
    }

    override suspend fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel): Boolean {
        return when {
            db.getById(radioModel.id) != null -> db.deleteById(radioModel.id) > 0
            else -> db.save(radioModel.copy(isFavorite = true).toEntity()) > 0
        }
    }

    override suspend fun searchFavoriteRadioStations(text: String): Flow<List<RadioModel>> {
        return when {
            text.isNotEmpty() -> db.searchByName(text).map { it.toModel() }
            else -> db.getAll().map { it.toModel() }
        }
    }
}

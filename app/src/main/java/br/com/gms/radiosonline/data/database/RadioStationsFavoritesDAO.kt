package br.com.gms.radiosonline.data.database

import androidx.room.*
import br.com.gms.radiosonline.data.model.local.RadioEntity
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioStationsFavoritesDAO {

    @Insert
    suspend fun save(model: RadioEntity)

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE isFavorite = 1")
    fun getAll(): Flow<List<RadioEntity>>

    @Query("DELETE FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE id = :id AND isFavorite = 1")
    suspend fun getById(id: String): RadioEntity?

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE isFavorite = 1 AND name LIKE '%' || :text || '%'")
    fun searchByName(text: String): Flow<List<RadioEntity>>
}

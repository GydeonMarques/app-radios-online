package br.com.gms.webradios.data.database

import androidx.room.*
import br.com.gms.webradios.data.model.local.RadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioStationsFavoritesDAO {

    @Insert
    suspend fun save(model: RadioEntity): Long

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE isFavorite = 1")
    fun getAll(): Flow<List<RadioEntity>>

    @Query("DELETE FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE id = :id AND isFavorite = 1")
    suspend fun getById(id: String): RadioEntity?

    @Query("SELECT * FROM ${DBConstants.FAVORITE_RADIO_TABLE_NAME} WHERE isFavorite = 1 AND name LIKE '%' || :text || '%'")
    fun searchByName(text: String): Flow<List<RadioEntity>>
}

package br.com.encoding.dreams.radios.online.data.database

import androidx.room.*
import br.com.encoding.dreams.radios.online.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalRadioStationsDao {
    @Insert
    suspend fun insert(model: RadioModel)

    @Query("DELETE FROM radio WHERE streamURL = :stream")
    suspend fun deleteRadioByStream(stream: String)

    @Query("SELECT * FROM radio WHERE isFavorite = 1")
    fun findAllFavorites(): Flow<List<RadioModel>>

    @Query("SELECT * FROM category")
    suspend fun getRadioStationCategories(): List<RadioCategoryModel>

    @Insert
    @Transaction
    suspend fun saveRadioStationCategories(categories: Set<RadioCategoryModel>)

    @Update
    @Transaction
    suspend fun updateRadioStationCategories(categories: Set<RadioCategoryModel>)

    @Query("SELECT * FROM radio WHERE isFavorite = 1 AND name LIKE '%' || :text || '%'")
    fun searchFavorites(text: String): Flow<List<RadioModel>>

    @Query("SELECT * FROM radio WHERE streamURL = :streamURL AND isFavorite = 1")
    suspend fun findFavoriteByStream(streamURL: String): RadioModel?

    @Query("DELETE FROM category")
    suspend fun clearAllRadioStationCategories()

    @Query("DELETE FROM category WHERE name = :category")
    suspend fun deleteCategory(category: String)

}

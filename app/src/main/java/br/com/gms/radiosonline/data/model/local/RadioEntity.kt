package br.com.gms.radiosonline.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.gms.radiosonline.data.database.DBConstants

@Entity(tableName = DBConstants.FAVORITE_RADIO_TABLE_NAME)
data class RadioEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val state: String,
    val country: String,
    val logoUrl: String,
    var category: String,
    val streamUrl: String,
    val description: String,
    val website: String? = null,
    var isFavorite: Boolean = false
)
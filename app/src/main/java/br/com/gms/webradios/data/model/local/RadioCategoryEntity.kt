package br.com.gms.webradios.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.gms.webradios.data.database.DBConstants

@Entity(tableName = DBConstants.RADIO_CATEGORY_TABLE_NAME)
data class RadioCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    var selected: Boolean = false,
)
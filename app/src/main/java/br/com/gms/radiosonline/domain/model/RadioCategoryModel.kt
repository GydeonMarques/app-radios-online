package br.com.encoding.dreams.radios.online.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category")
data class RadioCategoryModel(
    @PrimaryKey
    val name: String,
    var selected: Boolean = false,
) : Parcelable
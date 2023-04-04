package br.com.gms.radiosonline.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category")
data class RadioCategoryModel(
    @PrimaryKey
    val id: String,
    val name: String,
    var selected: Boolean = false,
) : Parcelable
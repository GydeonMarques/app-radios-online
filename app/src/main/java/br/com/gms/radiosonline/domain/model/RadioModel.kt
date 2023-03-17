package br.com.gms.radiosonline.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "radio")
data class RadioModel(
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
    var topRadio: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable
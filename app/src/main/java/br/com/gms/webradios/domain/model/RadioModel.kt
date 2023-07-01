package br.com.gms.webradios.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioModel(
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
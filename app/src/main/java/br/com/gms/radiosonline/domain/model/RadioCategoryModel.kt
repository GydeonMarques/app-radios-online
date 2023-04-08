package br.com.gms.radiosonline.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioCategoryModel(
    val id: String,
    val name: String,
    var selected: Boolean = false,
) : Parcelable
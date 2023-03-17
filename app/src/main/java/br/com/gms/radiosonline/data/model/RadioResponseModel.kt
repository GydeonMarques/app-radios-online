package br.com.gms.radiosonline.data.model

import android.os.Parcelable
import br.com.gms.radiosonline.domain.model.RadioModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioResponseModel(
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
    val topRadio: Boolean = false
): Parcelable

fun DocumentSnapshot.toModel(): RadioResponseModel {
    return RadioResponseModel(
        id = id,
        name = getString("name") ?: "",
        city = getString("city") ?: "",
        state = getString("state") ?: "",
        website = getString("website") ?: "",
        logoUrl = getString("logoUrl") ?: "",
        country = getString("country") ?: "",
        category = getString("category") ?: "",
        streamUrl = getString("streamUrl") ?: "",
        topRadio = getBoolean("topRadio") ?: false,
        description = getString("description") ?: ""
    )
}

fun RadioResponseModel.toModel(): RadioModel {
    return RadioModel(
        id = id,
        name = name,
        city = city,
        state = state,
        website = website,
        logoUrl = logoUrl,
        country = country,
        category = category,
        topRadio = topRadio,
        streamUrl = streamUrl,
        description = description,
    )
}
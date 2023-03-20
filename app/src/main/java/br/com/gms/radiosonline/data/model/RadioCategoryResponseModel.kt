package br.com.gms.radiosonline.data.model

import android.os.Parcelable
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioCategoryResponseModel(
    val id: String,
    val name: String,
) : Parcelable

fun DocumentSnapshot.toRadioCategoryResponse(): RadioCategoryResponseModel {
    return RadioCategoryResponseModel(
        id = id,
        name = getString("name") ?: "",
    )
}

fun RadioCategoryResponseModel.toRadioCategoryModel(): RadioCategoryModel {
    return RadioCategoryModel(
        id = id,
        name = name,
        selected = false,
    )
}
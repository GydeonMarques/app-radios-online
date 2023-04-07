package br.com.gms.radiosonline.data.model.mapper

import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toModel(): RadioModel {
    return RadioModel(
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

fun DocumentSnapshot.toRadioCategoryModel(): RadioCategoryModel {
    return RadioCategoryModel(
        id = id,
        name = getString("name") ?: "",
    )
}

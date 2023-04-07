package br.com.gms.radiosonline.data.model.mapper

import br.com.gms.radiosonline.data.model.local.RadioEntity
import br.com.gms.radiosonline.domain.model.RadioModel

fun RadioModel.toEntity(): RadioEntity {
    return RadioEntity(
        id = id,
        name = name,
        city = city,
        state = state,
        website = website,
        country = country,
        logoUrl = logoUrl,
        category = category,
        streamUrl = streamUrl,
        description = description,
        isFavorite = isFavorite
    )
}
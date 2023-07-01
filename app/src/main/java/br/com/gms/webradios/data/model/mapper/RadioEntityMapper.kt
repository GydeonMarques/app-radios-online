package br.com.gms.webradios.data.model.mapper

import br.com.gms.webradios.data.model.local.RadioEntity
import br.com.gms.webradios.domain.model.RadioModel

fun RadioEntity.toModel(): RadioModel {
    return RadioModel(
        id = id,
        name = name,
        city = city,
        state = state,
        website = website,
        country = country,
        logoUrl = logoUrl,
        topRadio = false,
        category = category,
        streamUrl = streamUrl,
        description = description,
        isFavorite = isFavorite
    )
}

fun List<RadioEntity>.toModel(): List<RadioModel> {
    return this.map { it.toModel() }
}
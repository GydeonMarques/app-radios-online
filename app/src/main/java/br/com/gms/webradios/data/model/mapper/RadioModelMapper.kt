package br.com.gms.webradios.data.model.mapper

import br.com.gms.webradios.data.model.local.RadioEntity
import br.com.gms.webradios.domain.model.RadioModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

suspend fun mapListOfRadiosByCategories(list: List<RadioModel>): MutableMap<String, ArrayList<RadioModel>> {
    return withContext(Dispatchers.Default) {
        mutableMapOf<String, ArrayList<RadioModel>>().also { items ->
            list.forEach { radioModel ->
                if (items.containsKey(radioModel.category)) {
                    if (items[radioModel.category]?.contains(radioModel) == false) {
                        items[radioModel.category]?.add(radioModel)
                    }
                } else {
                    items[radioModel.category] = arrayListOf(radioModel)
                }
            }
        }
    }
}
package br.com.gms.webradios.data.sample

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.gms.webradios.domain.model.RadioCategoryModel
import br.com.gms.webradios.domain.model.RadioModel
import java.util.UUID

val sampleRadioCategories = listOf(
    RadioCategoryModel(
        id = UUID.randomUUID().toString(),
        name = "Evangélica",
        selected = false
    ),
    RadioCategoryModel(
        id = UUID.randomUUID().toString(),
        name = "Sertanejo",
        selected = false
    )
)
val sampleRadiosList = listOf(
    RadioModel(
        id = UUID.randomUUID().toString(),
        name = LoremIpsum(3).values.first(),
        description = LoremIpsum(10).values.first(),
        logoUrl = "https://www.google.com.br/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
        streamUrl = LoremIpsum(5).values.first(),
        city = "São Paulo",
        country = "São Paulo",
        category = "Lorem Ypsum",
        website = "https://www.google.com",
        state = "SP"
    ),
    RadioModel(
        id = UUID.randomUUID().toString(),
        name = LoremIpsum(3).values.first(),
        description = LoremIpsum(10).values.first(),
        logoUrl = "https://www.google.com.br/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
        streamUrl = LoremIpsum(5).values.first(),
        city = "São Paulo",
        country = "São Paulo",
        category = "Lorem Ypsum",
        website = "https://www.google.com",
        state = "SP"
    ),
    RadioModel(
        id = UUID.randomUUID().toString(),
        name = LoremIpsum(3).values.first(),
        description = LoremIpsum(10).values.first(),
        logoUrl = "https://www.google.com.br/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
        streamUrl = LoremIpsum(5).values.first(),
        city = "São Paulo",
        country = "São Paulo",
        category = "Lorem Ypsum",
        website = "https://www.google.com",
        state = "SP"
    )
)
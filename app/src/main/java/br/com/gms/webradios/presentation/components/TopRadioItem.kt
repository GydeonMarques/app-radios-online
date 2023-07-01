package br.com.gms.webradios.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.webradios.data.sample.sampleRadiosList
import br.com.gms.webradios.domain.model.RadioModel
import br.com.gms.webradios.presentation.theme.GrayDark
import br.com.gms.webradios.presentation.theme.RadiosOnlineTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TopRadioItem(
    radioModel: RadioModel,
    onItemClickListener: (radio: RadioModel) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .width(120.dp)
            .clickable {
                onItemClickListener(radioModel)
            },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, color = GrayDark),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp,
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .fallback(br.com.gms.webradios.R.drawable.fallback_image)
                    .data(radioModel.logoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(br.com.gms.webradios.R.drawable.fallback_image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
            )
        }
    }
}

@Preview
@Composable
private fun TopRadioItemPreview() {
    RadiosOnlineTheme {
        TopRadioItem(
            radioModel = sampleRadiosList.random()
        )
    }
}
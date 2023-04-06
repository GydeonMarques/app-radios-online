package br.com.gms.radiosonline.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.data.sample.sampleRadiosList
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.presentation.theme.DefaultDividerHeight
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun PlayingNowItem(
    radioModel: RadioModel,
) {

    val soundWave by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_loading))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium),
    ) {
        Row(
            horizontalArrangement= Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color = MaterialTheme.colors.background)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .fallback(R.drawable.fallback_image)
                    .data(radioModel.logoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.fallback_image),
                contentDescription = radioModel.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Box(modifier = Modifier
                .padding(4.dp)
                .weight(1f)) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = radioModel.name,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.h3,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(DefaultDividerHeight))
                    Text(
                        maxLines = 3,
                        text = stringResource(id = R.string.playing_now),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondary
                    )

                }
            }
            Spacer(modifier = Modifier.height(DefaultDividerHeight))
            LottieAnimation(
                soundWave,
                iterations = 999999999,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingNowItemPreview(){
    RadiosOnlineTheme {
        PlayingNowItem(
            radioModel = sampleRadiosList.first()
        )
    }
}
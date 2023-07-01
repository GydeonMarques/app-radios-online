package br.com.gms.webradios.presentation.screens.playing_now

import android.content.Intent
import android.net.Uri
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gms.webradios.R
import br.com.gms.webradios.presentation.components.UiViewState
import br.com.gms.webradios.presentation.theme.DefaultPadding
import br.com.gms.webradios.presentation.theme.DefaultPaddingMin
import br.com.gms.webradios.presentation.theme.DefaultRadiusMin
import br.com.gms.webradios.presentation.theme.RadiosOnlineTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun PlayingNowContent(
    viewModel: PlayingNowViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.playingNowUiState.collectAsState()
    val playbackState by viewModel.playbackState.observeAsState()
    val radioStationPlayingNow by viewModel.currentPlayingMedia.observeAsState()

    val icPower by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_power))
    val icLoading by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ic_loading))

    when (uiState) {
        is PlayingNowUiState.Nothing -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.warning),
                message = stringResource(R.string.there_are_currently_no_radios_running)
            )
        }
        is PlayingNowUiState.Loading -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_loading),
                message = stringResource(R.string.please_wait_a_moment_while_we_load_the_data)
            )
        }
        is PlayingNowUiState.Failure -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                message = stringResource(R.string.there_was_a_failure_loading_the_data)
            )
        }
        is PlayingNowUiState.Success -> {

            (uiState as PlayingNowUiState.Success).radioStation?.let { radio ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = DefaultPadding,
                                end = DefaultPadding,
                                bottom = 40.dp,
                                start = DefaultPadding
                            )
                            .background(MaterialTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .background(MaterialTheme.colors.background),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .fallback(R.drawable.fallback_image)
                                    .crossfade(true)
                                    .data(radio.logoUrl)
                                    .build(),
                                contentDescription = radio.description,
                                placeholder = painterResource(id = R.drawable.fallback_image),
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(275.dp)
                                    .clip(RoundedCornerShape(DefaultRadiusMin))
                            )

                            Spacer(modifier = Modifier.height(DefaultPadding))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = radio.name,
                                    style = MaterialTheme.typography.h1,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${radio.city} - ${radio.state} - ${radio.country}",
                                    style = MaterialTheme.typography.subtitle2,
                                    modifier = Modifier.padding(end = DefaultPaddingMin),
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            Spacer(modifier = Modifier.height(DefaultPadding))

                            Text(
                                text = radio.description,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.subtitle2,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(DefaultPadding))

                            radio.website?.let {
                                Text(
                                    text = stringResource(R.string.visit_our_website_via_the_link_below),
                                    modifier = Modifier.padding(top = DefaultPaddingMin),
                                    style = MaterialTheme.typography.subtitle2,
                                )

                                Text(
                                    text = "${radio.website}",
                                    modifier = Modifier.clickable {
                                        startActivity(context, Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(radio.website)
                                        }, null)
                                    },
                                    style = MaterialTheme.typography.subtitle2,
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colors.secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(DefaultPadding))

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                IconButton(
                                    modifier = Modifier.size(50.dp),
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = stringResource(
                                            id = R.string.share,
                                            radio.name
                                        ),
                                        tint = MaterialTheme.colors.secondary,
                                        modifier = Modifier
                                            .size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(DefaultPaddingMin))

                                when (playbackState?.state) {
                                    PlaybackStateCompat.STATE_BUFFERING -> {
                                        LottieAnimation(
                                            icLoading,
                                            iterations = 999999999,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    radio.let {
                                                        viewModel.play(it)
                                                    }
                                                },
                                        )
                                    }
                                    PlaybackStateCompat.STATE_PLAYING -> {
                                        if (radio.id == radioStationPlayingNow?.id) {
                                            LottieAnimation(
                                                icPower,
                                                iterations = 999999999,
                                                modifier = Modifier
                                                    .size(90.dp)
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        radio.let {
                                                            viewModel.pause()
                                                        }
                                                    },
                                            )
                                        } else {
                                            LottieAnimation(
                                                icPower,
                                                isPlaying = false,
                                                modifier = Modifier
                                                    .size(90.dp)
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        radio.let {
                                                            viewModel.play(it)
                                                        }
                                                    },
                                            )
                                        }
                                    }
                                    else -> {
                                        LottieAnimation(
                                            icPower,
                                            isPlaying = false,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    radio.let {
                                                        viewModel.play(it)
                                                    }
                                                },
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(DefaultPaddingMin))

                                IconButton(
                                    modifier = Modifier
                                        .size(50.dp),
                                    onClick = {
                                        viewModel.addOrRemoveRadioStationFromFavorites(radio)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (radio.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = radio.description,
                                        tint = MaterialTheme.colors.secondary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            } ?: run {
                UiViewState(
                    icon = LottieCompositionSpec.RawRes(R.raw.warning),
                    message = stringResource(R.string.there_are_currently_no_radios_running)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayingNowScreenPreview() {
    RadiosOnlineTheme {
        PlayingNowContent(
            viewModel = hiltViewModel()
        )
    }
}
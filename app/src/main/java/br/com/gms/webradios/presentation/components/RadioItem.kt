package br.com.gms.webradios.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
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
import br.com.gms.webradios.R
import br.com.gms.webradios.data.sample.sampleRadiosList
import br.com.gms.webradios.domain.model.RadioModel
import br.com.gms.webradios.presentation.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun RadioItem(
    radioModel: RadioModel,
    modifier: Modifier = Modifier,
    onItemClickListener: (radio: RadioModel) -> Unit = {},
    addOrRemoveFromFavorites: (radio: RadioModel) -> Unit = {},
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onItemClickListener(radioModel)
            },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
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
                    .width(DefaultListItemImageSize)
                    .height(DefaultListItemImageSize)
                    .clip(MaterialTheme.shapes.medium)
            )
            Box(modifier = Modifier.padding(4.dp)) {
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
                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = {
                                addOrRemoveFromFavorites(radioModel)
                            }
                        ) {
                            Icon(
                                imageVector = if (radioModel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                tint = if (radioModel.isFavorite) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface,
                                contentDescription = stringResource(
                                    R.string.add_to_favorites,
                                    radioModel.name
                                ),
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(DefaultDividerHeight))
                    Text(
                        maxLines = 3,
                        text = "${radioModel.city} - ${radioModel.state}",
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.subtitle2
                    )
                    Spacer(modifier = Modifier.height(DefaultDividerHeight))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = radioModel.country,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.weight(1f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RadioItemPreview() {
    RadiosOnlineTheme {
        RadioItem(
            radioModel = sampleRadiosList.random()
        )
    }
}
package br.com.gms.webradios.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.gms.webradios.R
import br.com.gms.webradios.data.sample.sampleRadiosList
import br.com.gms.webradios.domain.model.RadioModel
import br.com.gms.webradios.presentation.theme.DefaultPadding
import br.com.gms.webradios.presentation.theme.RadiosOnlineTheme

@Composable
fun ItemHeader(
    radios: List<RadioModel>,
    modifier: Modifier = Modifier,
    onItemClickListener: (radio: RadioModel) -> Unit = {}
) {
    Column(
        modifier = modifier.background(color = MaterialTheme.colors.background),
    ) {
        Text(
            text = stringResource(id = R.string.top_radios),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h2,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = DefaultPadding),
            horizontalArrangement = Arrangement.spacedBy(DefaultPadding),
        ) {
            itemsIndexed(radios) { index, radio ->
                TopRadioItem(
                    radioModel = radio,
                    onItemClickListener = {
                        onItemClickListener(it)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemHeaderPreview() {
    RadiosOnlineTheme {
        ItemHeader(
            radios = sampleRadiosList
        )
    }
}
package br.com.gms.radiosonline.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.gms.radiosonline.presentation.theme.DefaultRadiusMax
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme

@Composable
fun CustomTopAppBar(
    title: String,
) {

    TopAppBar(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEnd = DefaultRadiusMax,
                bottomStart = DefaultRadiusMax
            )
        )
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1.copy(
                    color = MaterialTheme.colors.secondary
                )
            )
        }
    }
}

@Composable
@Preview("Toolbar padrão do app", showBackground = true)
private fun MyTopAppBarPreview() {
    RadiosOnlineTheme {
        CustomTopAppBar(title = "Estações de Rádios")
    }
}
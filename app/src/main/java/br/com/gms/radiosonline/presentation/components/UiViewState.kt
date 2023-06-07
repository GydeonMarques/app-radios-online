package br.com.gms.radiosonline.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.presentation.theme.DefaultPadding
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun UiViewState(
    modifier: Modifier = Modifier,
    icon: LottieCompositionSpec,
    message: String,
) {
    val icFavorite by rememberLottieComposition(icon)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = DefaultPadding,
                end = DefaultPadding,
                start = DefaultPadding,
                bottom = 105.dp
            )
    ) {
        LottieAnimation(
            icFavorite,
            iterations = 1,
            isPlaying = true,
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UiViewStatePreview() {
    RadiosOnlineTheme {
        UiViewState(
            icon = LottieCompositionSpec.RawRes(br.com.gms.radiosonline.R.raw.ic_radio),
            message = "Esta e uma view padrão que tem por finalidade exibir messagens de alerta durante o processamento de dados. Ex: Loading, error... etc."
        )
    }
}
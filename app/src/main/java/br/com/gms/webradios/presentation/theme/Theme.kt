package br.com.gms.webradios.presentation.theme

import android.view.Window
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb

private val LightThemeColors = lightColors(
    primary = Dark,
    primaryVariant = Dark_1,
    secondary = Pink,
    secondaryVariant = AlphaPink,
    background = Dark,
    onSurface = GrayLight,
)

private val DarkThemeColors = darkColors(
    primary = Dark,
    primaryVariant = Dark_1,
    secondary = Pink,
    secondaryVariant = AlphaPink,
    background = Dark,
    onSurface = GrayLight,
)

@Composable
fun SystemUi(windows: Window) =
    MaterialTheme {
        windows.statusBarColor = MaterialTheme.colors.secondary.toArgb()
        windows.navigationBarColor = MaterialTheme.colors.secondary.toArgb()
    }

@Composable
fun RadiosOnlineTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }
    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content,
        typography = Typography
    )
}
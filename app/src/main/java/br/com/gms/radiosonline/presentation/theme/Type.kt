package br.com.gms.radiosonline.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.gms.radiosonline.R

val MontserratFontFamily = FontFamily(
    listOf(
        Font(R.font.montserrat_regular),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold)
    )
)

val Typography = Typography(
    defaultFontFamily = MontserratFontFamily,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = GrayLight,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = GrayLight
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = GrayLight
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = GrayLight
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = GrayLight
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        color = GrayLight
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        color = GrayLight
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        color = GrayLight
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        color = GrayLight
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        color = GrayLight
    ),
    button = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        color = GrayLight
    ),
    caption = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        color = GrayLight
    ),
    overline = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        color = GrayLight
    )
)

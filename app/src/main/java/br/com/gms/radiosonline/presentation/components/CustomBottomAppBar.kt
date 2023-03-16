package br.com.gms.radiosonline.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.twotone.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.presentation.theme.DefaultRadiusMax
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme

val bottomAppBarItems = listOf(
    BottomAppBarItem.RadioStations,
    BottomAppBarItem.PlayingNow,
    BottomAppBarItem.Favorites,
)

sealed class BottomAppBarItem(
    val label: String,
    val icon: ImageVector,
) {
    object RadioStations : BottomAppBarItem(
        label = "Radios",
        icon = Icons.Rounded.Home,
    )

    object PlayingNow : BottomAppBarItem(
        label = "Tocando agora",
        icon = Icons.Rounded.PlayCircle,
    )

    object Favorites : BottomAppBarItem(
        label = "Favoritos",
        icon = Icons.Rounded.Favorite,
    )
}

@Composable
fun CustomBottomAppBar(
    items: List<BottomAppBarItem> = emptyList(),
    currentItem: BottomAppBarItem = BottomAppBarItem.RadioStations,
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
) {

    BottomAppBar(
        modifier = Modifier
            .height(70.dp)
            .clip(
                RoundedCornerShape(
                    topEnd = DefaultRadiusMax,
                    topStart = DefaultRadiusMax
                )
            ),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        cutoutShape = MaterialTheme.shapes.medium.copy(
            CornerSize(percent = 50)
        )
    ) {

        items.forEach {

            val label = it.label
            val icon = it.icon

            BottomNavigationItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        modifier = Modifier.size(38.dp),
                        tint = if (currentItem.label == label) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface,
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (currentItem.label == label) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface
                    )
                },
                selected = currentItem.label == label,
                onClick = {
                    onBottomAppBarItemSelectedChange(it)
                }
            )
        }
    }
}

@Preview
@Composable
fun MyBottomAppBarPreview() {
    RadiosOnlineTheme {
        CustomBottomAppBar(
            items = listOf(
                BottomAppBarItem.RadioStations,
                BottomAppBarItem.PlayingNow,
                BottomAppBarItem.Favorites,
            )
        )
    }
}
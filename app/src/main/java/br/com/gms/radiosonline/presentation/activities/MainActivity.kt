package br.com.gms.radiosonline.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.presentation.components.BottomAppBarItem
import br.com.gms.radiosonline.presentation.components.CustomBottomAppBar
import br.com.gms.radiosonline.presentation.components.CustomTopAppBar
import br.com.gms.radiosonline.presentation.components.bottomAppBarItems
import br.com.gms.radiosonline.presentation.navigation.*
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme
import br.com.gms.radiosonline.presentation.theme.SystemUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadiosOnlineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SystemUi(window)
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {

    val radioStations = stringResource(id = R.string.radio_stations)
    var toolbarTitle by remember { mutableStateOf(radioStations) }
    val context = LocalContext.current

    val navController = rememberNavController()
    val backStackEntryState by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntryState?.destination

    val selectedItem by remember(currentDestination) {
        mutableStateOf(
            when {
                currentDestination?.route?.startsWith(radioStationsRoute) == true -> {
                    BottomAppBarItem.RadioStations
                }
                currentDestination?.route?.startsWith(playingNowRoute) == true -> {
                    BottomAppBarItem.PlayingNow
                }
                currentDestination?.route?.startsWith(favoritesRoute) == true -> {
                    BottomAppBarItem.Favorites
                }
                else -> {
                    BottomAppBarItem.RadioStations
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = toolbarTitle,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.size(70.dp),
                onClick = {
                    toolbarTitle = context.getString(R.string.playing_now)
                    navController.navigateToPlayingNow()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_wi_fi),
                    contentDescription = stringResource(id = R.string.playing_now),
                    modifier = Modifier.size(38.dp),
                    tint = if (selectedItem == BottomAppBarItem.PlayingNow) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface
                )
            }
        },
        bottomBar = {
            CustomBottomAppBar(
                items = bottomAppBarItems,
                currentItem = selectedItem,
                onBottomAppBarItemSelectedChange = { bottomAppBarItem ->
                    when (bottomAppBarItem) {
                        is BottomAppBarItem.RadioStations -> {
                            toolbarTitle = context.getString(R.string.radio_stations)
                            navController.navigateToRadioStations(navOptions {
                                launchSingleTop = true
                                popUpTo(radioStationsRoute)
                            })
                        }
                        is BottomAppBarItem.PlayingNow -> {
                            toolbarTitle = context.getString(R.string.playing_now)
                            navController.navigateToPlayingNow(navOptions {
                                launchSingleTop = true
                                popUpTo(playingNowRoute)
                            })
                        }
                        is BottomAppBarItem.Favorites -> {
                            toolbarTitle = context.getString(R.string.favorites)
                            navController.navigateToFavorites(navOptions {
                                launchSingleTop = true
                                popUpTo(favoritesRoute)
                            })
                        }
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            RadioOnlineNavHost(navController = navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RadiosOnlineAppPreview() {
    RadiosOnlineTheme {
        MainContent()
    }
}
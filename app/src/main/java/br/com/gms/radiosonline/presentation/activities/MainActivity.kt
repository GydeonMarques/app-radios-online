package br.com.gms.radiosonline.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import br.com.gms.radiosonline.presentation.navigation.RadioOnlineNavHost
import br.com.gms.radiosonline.presentation.navigation.favoritesRoute
import br.com.gms.radiosonline.presentation.navigation.navigateToFavorites
import br.com.gms.radiosonline.presentation.navigation.navigateToPlayingNow
import br.com.gms.radiosonline.presentation.navigation.navigateToRadioStations
import br.com.gms.radiosonline.presentation.navigation.playingNowRoute
import br.com.gms.radiosonline.presentation.navigation.radioStationsRoute
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsViewModelFactory
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme
import br.com.gms.radiosonline.presentation.theme.SystemUi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: RadioStationsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadiosOnlineTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SystemUi(window)
                    MainContent(viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun MainContent(viewModelFactory: RadioStationsViewModelFactory) {

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
            RadioOnlineNavHost(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }
    }
}
package br.com.gms.radiosonline.presentation.screens.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.presentation.components.CustomDialog
import br.com.gms.radiosonline.presentation.components.RadioItem
import br.com.gms.radiosonline.presentation.components.UiViewState
import br.com.gms.radiosonline.presentation.theme.DefaultPadding
import br.com.gms.radiosonline.presentation.theme.DefaultRadius
import br.com.gms.radiosonline.presentation.theme.DefaultRadiusMin
import br.com.gms.radiosonline.presentation.theme.GrayLight
import com.airbnb.lottie.compose.LottieCompositionSpec

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesRadiosScreen(
    viewModel: FavoritesViewModel,
    onRadioNavigationToPlayer: (radioId: String) -> Unit = {}
) {

    var searchText by rememberSaveable { mutableStateOf("") }
    var radioModel by rememberSaveable { mutableStateOf<RadioModel?>(null) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    val radioFavoritesUiState by viewModel.radioStationsFavoritesUiState.collectAsState()

    when (radioFavoritesUiState) {
        is RadioStationsFavoritesUiState.Loading -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_loading),
                message = stringResource(R.string.please_wait_a_moment_while_we_load_the_data)
            )
        }
        is RadioStationsFavoritesUiState.Failure -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                message = stringResource(R.string.there_are_currently_no_radio_stations_available)
            )
        }
        is RadioStationsFavoritesUiState.Success -> {

            val listRadioStations = (radioFavoritesUiState as RadioStationsFavoritesUiState.Success).radios

            if (listRadioStations.isEmpty() && searchText.isBlank()) {

                UiViewState(
                    icon = LottieCompositionSpec.RawRes(R.raw.ic_favorite),
                    message = stringResource(R.string.you_have_not_yet_added_any_radios_to_your_favorites)
                )

            } else {

                if (showConfirmationDialog) {
                    CustomDialog(
                        title = stringResource(R.string.attention),
                        subTitle = stringResource(R.string.really_want_to_remove_this_radio_from_your_favorites_list),
                        negativeButtonText = stringResource(R.string.cancel),
                        positiveButtonText = stringResource(R.string.yes),
                        onClickCancel = {
                            showConfirmationDialog = false
                        },
                        onClickConfirm = {
                            showConfirmationDialog = false
                            radioModel?.let { viewModel.removeRadioStationFromFavorites(it) }
                        },
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {

                    Text(
                        text = stringResource(id = R.string.listen_to_your_favorite_radios_all_in_one_place),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DefaultPadding),
                        style = MaterialTheme.typography.subtitle2,
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = searchText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = DefaultPadding,
                                start = DefaultPadding
                            ),
                        shape = RoundedCornerShape(DefaultRadiusMin),
                        onValueChange = {
                            searchText = it
                            viewModel.searchFavoriteRadioStations(it)
                        },

                        label = { Text(text = stringResource(id = R.string.search_radio)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(id = R.string.search),
                                tint = MaterialTheme.colors.onSurface
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.onSurface,
                            unfocusedBorderColor = MaterialTheme.colors.onSurface,
                            cursorColor = MaterialTheme.colors.onSurface
                        )
                    )

                    if (listRadioStations.isEmpty() && searchText.isNotBlank()) {
                        UiViewState(
                            icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                            message = stringResource(
                                R.string.no_results_found_for_your_search,
                                searchText
                            )
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = DefaultPadding),
                            verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                        )  {

                            listRadioStations.forEach { (category, radios) ->
                                stickyHeader {
                                    Text(
                                        text = category,
                                        Modifier
                                            .background(MaterialTheme.colors.background)
                                            .padding(vertical = DefaultPadding)
                                            .fillMaxWidth(),
                                        style = MaterialTheme.typography.h2
                                    )

                                    Divider(
                                        color = GrayLight,
                                    )
                                }

                                items(radios) { radio ->
                                    RadioItem(
                                        radioModel = radio,
                                        onItemClickListener = { radioModel ->
                                            onRadioNavigationToPlayer(radioModel.id)
                                        },
                                        addOrRemoveFromFavorites = {
                                            showConfirmationDialog = true
                                            radioModel = it
                                        }
                                    )
                                }
                            }

                            item {
                                Box(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .background(color = MaterialTheme.colors.background)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.presentation.components.ItemHeader
import br.com.gms.radiosonline.presentation.components.RadioItem
import br.com.gms.radiosonline.presentation.components.UiViewState
import br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog.RadioCategoryFilterDialog
import br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog.RadioCategoryUiState
import br.com.gms.radiosonline.presentation.theme.*
import com.airbnb.lottie.compose.LottieCompositionSpec
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadioStationsScreen(
    viewModel: RadioStationsViewModel,
    onRadioNavigationToPlayer: (radioId: String) -> Unit = {}
) {

    val appliedFilter by viewModel.appliedFilter.collectAsState()
    val radioUiState by viewModel.radioStationsUiState.collectAsState()
    val categoriesUiState by viewModel.radioCategoriesUiState.collectAsState()

    var searchText by rememberSaveable { mutableStateOf("") }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    if (showFilterDialog) {
        RadioCategoryFilterDialog(
            uiState = categoriesUiState,
            onClickCancel = {
                showFilterDialog = false
            },
            onClickFilter = {
                showFilterDialog = false
                viewModel.applyRadioStationFilter()
            },
            onCategorySelected = {
                viewModel.onCategorySelected(it)
            }
        )
    }

    when (radioUiState) {
        is RadioStationsUiState.Loading -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_loading),
                message = stringResource(R.string.please_wait_a_moment_while_we_load_the_data)
            )
        }
        is RadioStationsUiState.Failure -> {
            UiViewState(
                icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                message = stringResource(R.string.there_are_currently_no_radio_stations_available)
            )
        }
        is RadioStationsUiState.Success -> {

            val listRadioStations = (radioUiState as RadioStationsUiState.Success).listRadioStations
            val topRadioStations = (radioUiState as RadioStationsUiState.Success).topRadiosStations

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {

                Text(
                    text = stringResource(R.string.listen_to_your_favorite_radios_all_in_one_place),
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
                    onValueChange = {
                        searchText = it
                        viewModel.searchRadioStations(it)
                    },
                    label = { Text(stringResource(R.string.search_radio)) },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter_list),
                            contentDescription = stringResource(R.string.search),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(48.dp)
                                .padding(DefaultDividerHeightMin)
                                .clickable {
                                    showFilterDialog = true
                                }
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.onSurface,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface,
                        cursorColor = MaterialTheme.colors.onSurface
                    )
                )

                if (listRadioStations.isEmpty()) {
                    UiViewState(
                        icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                        message = if (appliedFilter) {
                            stringResource(
                                R.string.there_are_currently_no_radio_stations_available_for_the_applied_filter,
                            )
                        } else {
                            stringResource(
                                R.string.no_results_found_for_your_search,
                                searchText
                            )
                        }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = DefaultPadding),
                        verticalArrangement = Arrangement.spacedBy(DefaultPadding)
                    ) {

                        if (topRadioStations.isNotEmpty()) {
                            item {
                                ItemHeader(
                                    modifier = Modifier.padding(top = DefaultPadding),
                                    radios = topRadioStations,
                                    onItemClickListener = { radioModel ->
                                        onRadioNavigationToPlayer(radioModel.id)
                                    },
                                )
                            }
                        }

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
                                        viewModel.addOrRemoveRadioStationFromFavorites(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RadioStationsScreenPreview() {
    RadiosOnlineTheme {
        RadioStationsScreen(
            viewModel = hiltViewModel()
        )
    }
}

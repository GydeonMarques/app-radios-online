package br.com.gms.webradios.presentation.screens.radio_stations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.gms.webradios.R
import br.com.gms.webradios.presentation.components.UiViewState
import com.airbnb.lottie.compose.LottieCompositionSpec

@Composable
fun RadioStationsTabScreen(
    viewModel: RadioStationsViewModel,
    viewModelFactory: RadioStationsViewModelFactory,
    onRadioNavigationToPlayer: (radioId: String) -> Unit = {}
) {

    val categoriesUiState by viewModel.radioCategoriesUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        when (categoriesUiState) {
            is RadioCategoryUiState.Loading -> {
                UiViewState(
                    icon = LottieCompositionSpec.RawRes(R.raw.ic_loading),
                    message = stringResource(R.string.please_wait_a_moment_while_we_load_the_data)
                )
            }

            is RadioCategoryUiState.Failure -> {
                UiViewState(
                    icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                    message = stringResource(R.string.there_are_currently_no_radio_stations_available)
                )
            }

            is RadioCategoryUiState.Success -> {

                var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
                val categories = (categoriesUiState as RadioCategoryUiState.Success).categories

                if (categories.isNotEmpty()) {

                    ScrollableTabRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectedTabIndex = selectedTabIndex,
                        edgePadding = TabRowDefaults.DividerThickness,
                        backgroundColor = MaterialTheme.colors.background,
                    ) {
                        categories.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title) },
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index }
                            )
                        }
                    }

                    categories.forEachIndexed { index, category ->
                        if (index == selectedTabIndex) {
                            RadioStationsScreen(
                                category = category,
                                viewModel = viewModel(
                                    key = category,
                                    factory = viewModelFactory,
                                ),
                                onRadioNavigationToPlayer = onRadioNavigationToPlayer
                            )
                        }
                    }

                } else {
                    UiViewState(
                        icon = LottieCompositionSpec.RawRes(R.raw.ic_empty_list),
                        message = stringResource(R.string.there_are_currently_no_radio_stations_available)
                    )
                }
            }
        }
    }
}
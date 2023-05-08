package br.com.gms.radiosonline.presentation.screens.radio_stations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.presentation.components.UiViewState
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme
import com.airbnb.lottie.compose.LottieCompositionSpec

@Composable
fun RadioStationsTabScreen(
    viewModel: RadioStationsViewModel,
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
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.fillMaxWidth()
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
                                viewModel = viewModel,
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

@Preview
@Composable
fun RadioStationsScreenPreview() {
    RadiosOnlineTheme {
        RadioStationsTabScreen(hiltViewModel())
    }
}

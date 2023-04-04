package br.com.gms.radiosonline.presentation.screens.radio_stations.filter_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.data.sample.sampleRadioCategories
import br.com.gms.radiosonline.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.presentation.components.CustomDialog
import br.com.gms.radiosonline.presentation.theme.DefaultPaddingMin
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme


@Composable
fun RadioCategoryFilterDialog(
    uiState: RadioCategoryUiState,
    onClickCancel: () -> Unit = {},
    onClickFilter: () -> Unit = {},
    onCategorySelected: (category: RadioCategoryModel) -> Unit = {}
) {

    CustomDialog(
        title = stringResource(
            id = when (uiState) {
                is RadioCategoryUiState.Failure -> {
                    R.string.error
                }
                is RadioCategoryUiState.Loading -> {
                    R.string.loading
                }
                is RadioCategoryUiState.Success -> {
                    R.string.filter_radio
                    if (uiState.categories.isEmpty()) {
                        R.string.ops
                    } else {
                        R.string.filter_radio
                    }
                }
            }
        ),
        subTitle = stringResource(
            id = when (uiState) {
                is RadioCategoryUiState.Failure -> {
                    R.string.there_was_a_failure_loading_the_data
                }
                is RadioCategoryUiState.Loading -> {
                    R.string.please_wait_a_moment_while_we_load_the_data
                }
                is RadioCategoryUiState.Success -> {
                    if (uiState.categories.isEmpty()) {
                        R.string.no_categories_available
                    } else {
                        R.string.choose_which_radio_categories_you_want_to_view
                    }
                }
            }
        ),
        content = {

            when (uiState) {
                is RadioCategoryUiState.Failure -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                is RadioCategoryUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.onSurface,
                        )
                    }
                }
                is RadioCategoryUiState.Success -> {

                    val categories = uiState.categories

                    if (categories.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Rounded.ErrorOutline,
                                contentDescription = null,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categories) { category ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .clickable {
                                            onCategorySelected(category)
                                        }
                                ) {
                                    Checkbox(
                                        enabled = false,
                                        onCheckedChange = null,
                                        checked = category.selected,
                                        colors = CheckboxDefaults.colors(
                                            disabledColor = if (category.selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface.copy(
                                                alpha = ContentAlpha.disabled
                                            )
                                        )
                                    )
                                    Spacer(Modifier.width(DefaultPaddingMin))
                                    Text(text = category.name)
                                }

                            }
                        }
                    }
                }
            }

        },
        onClickCancel = { onClickCancel() },
        onClickConfirm = { onClickFilter() },
        negativeButtonText = stringResource(id = R.string.cancel),
        positiveButtonText = stringResource(id = R.string.filter),
        showPositiveButton = uiState is RadioCategoryUiState.Success && (uiState).categories.isNotEmpty()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RadioFilterDialogLoadingPreview() {
    RadiosOnlineTheme {
        RadioCategoryFilterDialog(
            uiState = RadioCategoryUiState.Loading
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RadioFilterDialogErrorPreview() {
    RadiosOnlineTheme {
        RadioCategoryFilterDialog(
            uiState = RadioCategoryUiState.Failure(
                error = Exception("Error")
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RadioFilterDialogEmptyPreview() {
    RadiosOnlineTheme {
        RadioCategoryFilterDialog(
            uiState = RadioCategoryUiState.Success(
                categories = emptyList()
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RadioFilterDialogSuccessPreview() {
    RadiosOnlineTheme {
        RadioCategoryFilterDialog(
            uiState = RadioCategoryUiState.Success(
                categories = sampleRadioCategories
            )
        )
    }
}
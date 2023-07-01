package br.com.gms.webradios.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import br.com.gms.webradios.presentation.theme.DefaultPadding
import br.com.gms.webradios.presentation.theme.DefaultPaddingMin
import br.com.gms.webradios.presentation.theme.DefaultRadiusMin
import br.com.gms.webradios.presentation.theme.RadiosOnlineTheme

@Composable
fun CustomDialog(
    title: String? = null,
    subTitle: String? = null,
    onClickCancel: () -> Unit = {},
    onClickConfirm: () -> Unit = {},
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    showPositiveButton: Boolean? = true,
    showNegativeButton: Boolean? = true,
    content: @Composable (() -> Unit)? = null,
) {
    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        title = {
            Column(
                verticalArrangement = Arrangement.spacedBy(DefaultPadding)
            ) {
                title?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                subTitle?.let {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        text = {
            content?.let {
                content()
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(
                    start = DefaultPadding,
                    end = DefaultPadding,
                    bottom = DefaultPadding
                ),
            ) {

                if (showNegativeButton == true && !negativeButtonText.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = { onClickCancel() },
                        border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = DefaultPaddingMin),
                        shape = RoundedCornerShape(DefaultRadiusMin),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background
                        ),
                        contentPadding = PaddingValues(DefaultPadding),
                    ) {
                        Text(text = negativeButtonText, color = MaterialTheme.colors.secondary)
                    }
                }

                if (showPositiveButton == true && !positiveButtonText.isNullOrBlank()) {
                    Button(
                        onClick = { onClickConfirm() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = DefaultPaddingMin),
                        shape = RoundedCornerShape(DefaultRadiusMin),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        contentPadding = PaddingValues(DefaultPadding),
                    ) {
                        Text(text = positiveButtonText, color = Color.White)
                    }
                }
            }
        },
        onDismissRequest = {},
        shape = RoundedCornerShape(DefaultRadiusMin),
    )
}

@Preview
@Composable
private fun MyDefaultDialogPreview() {
    RadiosOnlineTheme {
        CustomDialog(
            title = LoremIpsum(3).values.first(),
            subTitle = LoremIpsum(8).values.first(),
            positiveButtonText = "Ok",
            negativeButtonText = "Cancelar",
            content = {
                Text(text = LoremIpsum(20).values.first())
            },
        )
    }
}
package br.com.gms.radiosonline.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gms.radiosonline.presentation.theme.DefaultPadding
import br.com.gms.radiosonline.presentation.theme.DefaultPaddingMin
import br.com.gms.radiosonline.presentation.theme.DefaultRadiusMin
import br.com.gms.radiosonline.presentation.theme.RadiosOnlineTheme

@Composable
fun MyDefaultDialog(
    title: String? = null,
    subTitle: String? = null,
    onClickCancel: () -> Unit = {},
    onClickConfirm: () -> Unit = {},
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
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
                modifier = Modifier.padding(DefaultPadding),
            ) {

                if (!negativeButtonText.isNullOrBlank()) {
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
                        contentPadding = PaddingValues(
                            start = DefaultPadding,
                            top = DefaultPadding,
                            end = DefaultPadding,
                            bottom = DefaultPadding
                        ),
                    ) {
                        Text(text = negativeButtonText, color = MaterialTheme.colors.secondary)
                    }
                }

                if (!positiveButtonText.isNullOrBlank()) {
                    Button(
                        onClick = { onClickConfirm() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = DefaultPaddingMin),
                        shape = RoundedCornerShape(DefaultRadiusMin),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary
                        ),
                        contentPadding = PaddingValues(
                            start = DefaultPadding,
                            top = DefaultPadding,
                            end = DefaultPadding,
                            bottom = DefaultPadding
                        ),
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
        MyDefaultDialog(
            title = "O que é Lorem Ipsum?",
            subTitle = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI.",
            onClickCancel = {},
            onClickConfirm = {},
            positiveButtonText = "Ok",
            negativeButtonText = "Cancelar",
            content = {
                Text(text = "Contéudo extra poderá ser adicionado aqui, no corpo do dialog")
            },
        )
    }
}
package com.marcelo.souza.buscar.cep.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.theme.White
import com.patrik.fancycomposedialogs.dialogs.WarningFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun WarningDialog(
    title: String,
    message: String,
    isCancelable: Boolean,
    dialogButtonProperties: DialogButtonProperties,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    WarningFancyDialog(
        title = title,
        message = message,
        isCancelable = isCancelable,
        dialogActionType = DialogActionType.INFORMATIVE,
        dialogProperties = dialogButtonProperties,
        dialogStyle = DialogStyle.UPPER_CUTTING,
        neutralButtonClick = {
            onConfirmClick.invoke()
            onDismissClick.invoke()
        },
        dismissTouchOutside = onDismissClick
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewWarningDialog() {
    CEPTheme {
        WarningDialog(
            title = stringResource(R.string.title_continue_error_dialog),
            message = stringResource(id = R.string.message_continue_error_dialog),
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_ok_neutral_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = {},
            onDismissClick = {}
        )
    }
}
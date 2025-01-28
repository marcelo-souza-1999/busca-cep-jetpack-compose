package com.marcelo.souza.buscar.cep.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme

@Composable
fun FormOutlinedTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    isErrorEmpty: Boolean = false,
    isErrorInvalid: Boolean = false,
    errorMessageEmpty: String? = null,
    errorMessageInvalid: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int? = null
) {
    OutlinedTextField(
        value = value,
        enabled = enabled,
        onValueChange = { newValue ->
            if (maxLength == null || newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        label = { Text(text = label) },
        maxLines = 1,
        isError = isErrorEmpty || isErrorInvalid,
        supportingText = {
            if (isErrorEmpty) {
                Text(text = errorMessageEmpty ?: "", color = MaterialTheme.colorScheme.error)
            } else if (isErrorInvalid) {
                Text(text = errorMessageInvalid ?: "", color = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = cursorColor,
            focusedBorderColor = borderColor,
            disabledBorderColor = borderColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_20),
                end = dimensionResource(id = R.dimen.size_20),
                bottom = dimensionResource(id = R.dimen.size_12)
            )
    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
internal fun PreviewCustomOutlinedTextField() {
    CEPTheme {
        FormOutlinedTextField(
            value = "12213350",
            enabled = true,
            onValueChange = {},
            label = "Cep",
            keyboardType = KeyboardType.Number
        )
    }
}
package com.marcelo.souza.buscar.cep.domain.model

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

data class FieldsViewData(
    val value: String,
    val label: String,
    val isErrorEmpty: Boolean = false,
    val isErrorInvalid: Boolean = false,
    val enabled: Boolean = true,
    val modifier: Modifier = Modifier,
    val onValueChange: (String) -> Unit,
    val keyboardType: KeyboardType,
    val maxLength: Int? = null
)

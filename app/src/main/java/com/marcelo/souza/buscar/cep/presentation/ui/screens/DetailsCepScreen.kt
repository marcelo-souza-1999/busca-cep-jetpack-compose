package com.marcelo.souza.buscar.cep.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.presentation.components.FormOutlinedTextField
import com.marcelo.souza.buscar.cep.presentation.components.TopAppBar

@Composable
fun DetailsCepScreen(
    navController: NavController,
    cepData: CepViewData? = null
) {
    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        TopAppBar(
            title = stringResource(R.string.title_details_cep_top_app_bar),
            showBackButton = true,
            onBack = { navController.popBackStack() }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_12)))
            if (cepData != null) {
                val readOnlyFields = createReadOnlyFields(cepData)
                CreateFieldsList(fields = readOnlyFields)
            }
        }
    }
}

@Composable
private fun createReadOnlyFields(cepData: CepViewData): List<FieldsViewData> {
    return listOf(
        FieldsViewData(
            value = cepData.cep.orEmpty(),
            enabled = false,
            label = stringResource(R.string.label_cep),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.size_30)),
            onValueChange = { },
            keyboardType = KeyboardType.Number,
            maxLength = integerResource(R.integer.eight)
        ),
        FieldsViewData(
            value = cepData.street.orEmpty(),
            enabled = false,
            label = stringResource(R.string.label_street),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputStreet"),
            onValueChange = { },
            keyboardType = KeyboardType.Text
        ),
        FieldsViewData(
            value = cepData.neighborhood.orEmpty(),
            enabled = false,
            label = stringResource(R.string.label_neighborhood),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputNeighborhood"),
            onValueChange = { },
            keyboardType = KeyboardType.Text
        ),
        FieldsViewData(
            value = cepData.city.orEmpty(),
            enabled = false,
            label = stringResource(R.string.label_city),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputCity"),
            onValueChange = { },
            keyboardType = KeyboardType.Text
        ),
        FieldsViewData(
            value = cepData.state.orEmpty(),
            enabled = false,
            label = stringResource(R.string.label_state),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputState"),
            onValueChange = { },
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
private fun CreateFieldsList(fields: List<FieldsViewData>) {
    fields.forEach { field ->
        FormOutlinedTextField(
            value = field.value,
            enabled = field.enabled,
            onValueChange = field.onValueChange,
            isErrorEmpty = field.isErrorEmpty,
            isErrorInvalid = field.isErrorInvalid,
            errorMessageEmpty = stringResource(R.string.error_message_required_field),
            errorMessageInvalid = stringResource(R.string.error_message_invalid_field),
            label = field.label,
            modifier = field.modifier,
            keyboardType = field.keyboardType,
            maxLength = field.maxLength
        )
    }
}
package com.marcelo.souza.buscar.cep.presentation.ui.screens

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.presentation.components.FormOutlinedTextField
import com.marcelo.souza.buscar.cep.presentation.components.TopAppBar
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme

@Composable
fun DetailsCepScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        TopAppBar(
            title = context.getString(R.string.title_details_cep_top_app_bar)
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .imePadding()
        ) {
            val fields = listOf(
                FieldsViewData(
                    value = "12213-350",
                    enabled = false,
                    label = stringResource(R.string.text_label_cep),
                    isErrorEmpty = false,
                    isErrorInvalid = false,
                    modifier = Modifier
                        .testTag("inputCep")
                        .padding(top = dimensionResource(R.dimen.size_30)),
                    onValueChange = {},
                    keyboardType = KeyboardType.Number
                ),
                FieldsViewData(
                    value = "Rua José Bonifácio de Arantes",
                    enabled = false,
                    label = stringResource(R.string.text_label_street),
                    isErrorEmpty = false,
                    isErrorInvalid = false,
                    modifier = Modifier.testTag("inputStreet"),
                    onValueChange = {},
                    keyboardType = KeyboardType.Text
                ),
                FieldsViewData(
                    value = "Vila Paiva",
                    enabled = false,
                    label = stringResource(R.string.text_label_neighborhood),
                    isErrorEmpty = false,
                    isErrorInvalid = false,
                    modifier = Modifier.testTag("inputNeighborhood"),
                    onValueChange = {},
                    keyboardType = KeyboardType.Text
                ),
                FieldsViewData(
                    value = "São José dos Campos",
                    enabled = false,
                    label = stringResource(R.string.text_label_city),
                    isErrorEmpty = false,
                    isErrorInvalid = false,
                    modifier = Modifier.testTag("inputCity"),
                    onValueChange = {},
                    keyboardType = KeyboardType.Text
                ),
                FieldsViewData(
                    value = "São Paulo",
                    enabled = false,
                    label = stringResource(R.string.text_label_state),
                    isErrorEmpty = false,
                    isErrorInvalid = false,
                    modifier = Modifier.testTag("inputState"),
                    onValueChange = {},
                    keyboardType = KeyboardType.Text
                )
            )
            CepFormFields(fields)

            Spacer(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.size_12))
            )
        }
    }
}

@Composable
private fun CepFormFields(fields: List<FieldsViewData>) {
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
            modifier = field.modifier
        )
    }
}

@Preview(
    showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewDetailsCep() {
    CEPTheme {
        DetailsCepScreen(rememberNavController())
    }
}
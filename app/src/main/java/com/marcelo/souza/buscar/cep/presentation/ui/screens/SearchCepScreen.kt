package com.marcelo.souza.buscar.cep.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.presentation.components.FormOutlinedTextField
import com.marcelo.souza.buscar.cep.presentation.components.PrimaryButton
import com.marcelo.souza.buscar.cep.presentation.components.TopAppBar
import com.marcelo.souza.buscar.cep.presentation.theme.CEPTheme
import com.marcelo.souza.buscar.cep.presentation.viewmodel.CepViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchCepScreen(
    navController: NavController,
    viewModel: CepViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isLoading = false
    val initializeFields = getInitializedFields(viewModel)

    fun fetchGetDataCep(cep: String) = viewModel.getDataCep(cep)

    Scaffold(topBar = {
        TopAppBar(
            title = context.getString(R.string.title_search_cep_top_app_bar)
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .imePadding()
        ) {

            ShowFields(fields = initializeFields)

            Spacer(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_12))
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(R.dimen.size_8),
                        start = dimensionResource(R.dimen.size_12)
                    )
            ) {
                PrimaryButton(
                    onClickBtn = {

                    },
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.size_20)),
                    text = stringResource(R.string.text_btn_search_cep),
                    isLoading = false
                )

                PrimaryButton(
                    onClickBtn = { /* Lógica de ir pra proxima screen */ },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = dimensionResource(R.dimen.size_30),
                            end = dimensionResource(R.dimen.size_30)
                        ),
                    text = stringResource(R.string.text_btn_continue),
                    isLoading = false
                )
            }
        }
    }
}

@Composable
private fun getInitializedFields(viewModel: CepViewModel): List<FieldsViewData> {
    val cepValue by viewModel.cep.collectAsState()
    val streetValue by viewModel.street.collectAsState()
    val neighborhoodValue by viewModel.neighborhood.collectAsState()
    val cityValue by viewModel.city.collectAsState()
    val stateValue by viewModel.state.collectAsState()

    return listOf(
        FieldsViewData(
            value = cepValue,
            enabled = true,
            label = stringResource(R.string.label_cep),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .testTag("inputCep")
                .padding(top = dimensionResource(R.dimen.size_30)),
            onValueChange = viewModel::updateCep,
            keyboardType = KeyboardType.Number,
            maxLength = integerResource(R.integer.eight)
        ), FieldsViewData(
            value = streetValue,
            enabled = false,
            label = stringResource(R.string.label_street),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .testTag("inputStreet"),
            onValueChange = viewModel::updateStreet,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = neighborhoodValue,
            enabled = false,
            label = stringResource(R.string.label_neighborhood),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .testTag("inputNeighborhood"),
            onValueChange = viewModel::updateNeighborhood,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = cityValue,
            enabled = false,
            label = stringResource(R.string.label_city),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .testTag("inputCity"),
            onValueChange = viewModel::updateCity,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = stateValue,
            enabled = false,
            label = stringResource(R.string.label_state),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .testTag("inputState"),
            onValueChange = viewModel::updateState,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
private fun ShowFields(fields: List<FieldsViewData>) {
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

@Preview(
    showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PreviewSearchCep() {
    CEPTheme {
        SearchCepScreen(rememberNavController())
    }
}
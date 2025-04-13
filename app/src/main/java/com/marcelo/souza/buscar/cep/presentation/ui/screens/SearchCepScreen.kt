package com.marcelo.souza.buscar.cep.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.presentation.components.ErrorDialog
import com.marcelo.souza.buscar.cep.presentation.components.FormOutlinedTextField
import com.marcelo.souza.buscar.cep.presentation.components.PrimaryButton
import com.marcelo.souza.buscar.cep.presentation.components.TopAppBar
import com.marcelo.souza.buscar.cep.presentation.components.WarningDialog
import com.marcelo.souza.buscar.cep.presentation.theme.White
import com.marcelo.souza.buscar.cep.presentation.viewmodel.CepViewModel
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.EmptyCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.Error
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.InvalidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.NetworkError
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchCepScreen(
    navController: NavController,
    viewModel: CepViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val cepFocusRequester = remember { FocusRequester() }

    val cepValue by viewModel.cep.collectAsState()
    val streetValue by viewModel.street.collectAsState()
    val neighborhoodValue by viewModel.neighborhood.collectAsState()
    val cityValue by viewModel.city.collectAsState()
    val stateValue by viewModel.state.collectAsState()
    val viewStateGetDataCep by viewModel.viewState.collectAsState()
    val isLoading = viewStateGetDataCep is State.Loading

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorDialogTitle by remember { mutableStateOf("") }
    var errorDialogMessage by remember { mutableStateOf("") }
    var currentErrorType by remember { mutableStateOf("") }
    var showWarningDialog by remember { mutableStateOf(false) }

    val fieldsList = mutableListOf(
        FieldsViewData(
            value = cepValue,
            enabled = true,
            label = stringResource(R.string.label_cep),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.size_30))
                .focusRequester(cepFocusRequester),
            onValueChange = viewModel::updateCep,
            keyboardType = KeyboardType.Number,
            maxLength = integerResource(R.integer.eight)
        ), FieldsViewData(
            value = streetValue,
            enabled = false,
            label = stringResource(R.string.label_street),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputStreet"),
            onValueChange = viewModel::updateStreet,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = neighborhoodValue,
            enabled = false,
            label = stringResource(R.string.label_neighborhood),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputNeighborhood"),
            onValueChange = viewModel::updateNeighborhood,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = cityValue,
            enabled = false,
            label = stringResource(R.string.label_city),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputCity"),
            onValueChange = viewModel::updateCity,
            keyboardType = KeyboardType.Text
        ), FieldsViewData(
            value = stateValue,
            enabled = false,
            label = stringResource(R.string.label_state),
            isErrorEmpty = false,
            isErrorInvalid = false,
            modifier = Modifier.testTag("inputState"),
            onValueChange = viewModel::updateState,
            keyboardType = KeyboardType.Text
        )
    )

    fun fetchGetDataCep(cep: String) = viewModel.getDataCep(cep)

    LaunchedEffect(
        viewStateGetDataCep
    ) {
        when (val stateDataCep = viewStateGetDataCep) {
            is State.Success -> {
                if (stateDataCep.data.error == true) {
                    errorDialogTitle =
                        context.getString(R.string.title_get_cep_not_found_error_dialog)
                    errorDialogMessage =
                        context.getString(R.string.message_get_cep_not_found_error_dialog)
                    currentErrorType = ErrorConstants.NOT_FOUND
                    showErrorDialog = true
                } else {
                    focusManager.clearFocus()
                }
            }

            is State.Error -> {
                when (stateDataCep.errorType) {
                    EmptyCep -> {
                        errorDialogTitle =
                            context.getString(R.string.title_get_cep_empty_error_dialog)
                        errorDialogMessage =
                            context.getString(R.string.message_get_cep_empty_error_dialog)
                        currentErrorType = ErrorConstants.EMPTY
                        showErrorDialog = true
                    }

                    InvalidCep -> {
                        errorDialogTitle =
                            context.getString(R.string.title_get_cep_invalid_error_dialog)
                        errorDialogMessage =
                            context.getString(R.string.message_get_cep_invalid_error_dialog)
                        currentErrorType = ErrorConstants.INVALID
                        showErrorDialog = true
                    }

                    NetworkError -> {
                        errorDialogTitle =
                            context.getString(R.string.title_get_cep_network_error_dialog)
                        errorDialogMessage =
                            context.getString(R.string.message_get_cep_network_error_dialog)
                        currentErrorType = ErrorConstants.NETWORK
                        showErrorDialog = true
                    }

                    Error -> {
                        errorDialogTitle = context.getString(R.string.title_get_cep_error_dialog)
                        errorDialogMessage =
                            context.getString(R.string.message_get_cep_error_dialog)
                        currentErrorType = ErrorConstants.ERROR
                        showErrorDialog = true
                    }
                }
            }

            else -> {}
        }
    }

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
            ContentSection(
                fieldsList = fieldsList,
                isLoading = isLoading,
                onSearchClick = { fetchGetDataCep(cepValue) },
                onContinueClick = {
                    if (viewStateGetDataCep is State.Success) {
                        viewModel.clearCepStates()
                    } else {
                        showWarningDialog = true
                    }
                }
            )
        }

        DialogSection(
            showErrorDialog = showErrorDialog,
            errorDialogTitle = errorDialogTitle,
            errorDialogMessage = errorDialogMessage,
            onErrorDialogConfirm = {
                showErrorDialog = false
                when (currentErrorType) {
                    ErrorConstants.NETWORK -> fetchGetDataCep(cepValue)
                    ErrorConstants.EMPTY,
                    ErrorConstants.INVALID,
                    ErrorConstants.NOT_FOUND,
                    ErrorConstants.ERROR -> {
                        viewModel.updateCep("")
                        cepFocusRequester.requestFocus()
                    }

                    else -> {
                        viewModel.updateCep("")
                        cepFocusRequester.requestFocus()
                    }
                }
            },
            onErrorDialogDismiss = { showErrorDialog = false },
            showWarningDialog = showWarningDialog,
            onWarningDialogConfirm = { showWarningDialog = false },
            onWarningDialogDismiss = { showWarningDialog = false }
        )
    }

    LaunchedEffect(key1 = showErrorDialog) {
        if (!showErrorDialog &&
            currentErrorType in listOf(
                ErrorConstants.EMPTY,
                ErrorConstants.INVALID,
                ErrorConstants.NOT_FOUND,
                ErrorConstants.ERROR
            )
        ) {
            cepFocusRequester.requestFocus()
        }
    }
}

@Composable
private fun ContentSection(
    fieldsList: List<FieldsViewData>,
    isLoading: Boolean,
    onSearchClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    CreateFieldsList(fieldsList)

    Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_12)))

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(R.dimen.size_8),
                start = dimensionResource(R.dimen.size_12)
            )
    ) {
        PrimaryButton(
            onClickBtn = onSearchClick,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.size_20)),
            text = stringResource(R.string.text_btn_search_cep),
            isLoading = isLoading,
            enabled = !isLoading
        )
        PrimaryButton(
            onClickBtn = onContinueClick,
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

@Composable
private fun DialogSection(
    showErrorDialog: Boolean,
    errorDialogTitle: String,
    errorDialogMessage: String,
    onErrorDialogConfirm: () -> Unit,
    onErrorDialogDismiss: () -> Unit,
    showWarningDialog: Boolean,
    onWarningDialogConfirm: () -> Unit,
    onWarningDialogDismiss: () -> Unit
) {
    if (showErrorDialog) {
        ShowDialog(
            title = errorDialogTitle,
            message = errorDialogMessage,
            isErrorDialog = true,
            isWarningDialog = false,
            onDismissClick = onErrorDialogDismiss,
            onConfirmClick = onErrorDialogConfirm
        )
    }
    if (showWarningDialog) {
        ShowDialog(
            title = stringResource(R.string.title_continue_error_dialog),
            message = stringResource(R.string.message_continue_error_dialog),
            isErrorDialog = false,
            isWarningDialog = true,
            onDismissClick = onWarningDialogDismiss,
            onConfirmClick = onWarningDialogConfirm
        )
    }
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

@Composable
private fun ShowDialog(
    title: String,
    message: String,
    isErrorDialog: Boolean,
    isWarningDialog: Boolean,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (isErrorDialog) {
        ErrorDialog(
            title = title,
            message = message,
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                positiveButtonText = R.string.txt_btn_positive_error_dialog,
                negativeButtonText = R.string.txt_btn_negative_error_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onConfirmClick,
            onDismissClick = onDismissClick
        )
    }

    if (isWarningDialog) {
        WarningDialog(
            title = title,
            message = message,
            isCancelable = true,
            dialogButtonProperties = DialogButtonProperties(
                neutralButtonText = R.string.txt_btn_ok_neutral_dialog,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = White
            ),
            onConfirmClick = onConfirmClick,
            onDismissClick = onDismissClick
        )
    }
}

private object ErrorConstants {
    const val NOT_FOUND = "not_found"
    const val EMPTY = "empty"
    const val INVALID = "invalid"
    const val NETWORK = "network"
    const val ERROR = "error"
}
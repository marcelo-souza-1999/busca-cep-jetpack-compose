package com.marcelo.souza.buscar.cep.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.presentation.components.ErrorDialog
import com.marcelo.souza.buscar.cep.presentation.components.FormOutlinedTextField
import com.marcelo.souza.buscar.cep.presentation.components.PrimaryButton
import com.marcelo.souza.buscar.cep.presentation.components.TopAppBar
import com.marcelo.souza.buscar.cep.presentation.components.WarningDialog
import com.marcelo.souza.buscar.cep.presentation.theme.White
import com.marcelo.souza.buscar.cep.presentation.ui.navigation.Routes
import com.marcelo.souza.buscar.cep.presentation.ui.utils.Constants.CEP_DATA
import com.marcelo.souza.buscar.cep.presentation.viewmodel.CepViewModel
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.DialogState
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.InvalidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.NetworkError
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchCepScreen(
    navController: NavController
) {
    val viewModel: CepViewModel = koinViewModel()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val cepFocusRequester = remember { FocusRequester() }

    val cepValue by viewModel.cep.collectAsStateWithLifecycle()
    val streetValue by viewModel.street.collectAsStateWithLifecycle()
    val neighborhoodValue by viewModel.neighborhood.collectAsStateWithLifecycle()
    val cityValue by viewModel.city.collectAsStateWithLifecycle()
    val stateValue by viewModel.state.collectAsStateWithLifecycle()
    val viewStateGetDataCep by viewModel.viewState.collectAsStateWithLifecycle()
    val isLoading = viewStateGetDataCep is State.Loading

    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }

    val fieldsList = listOf(
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

    fun fetchGetDataCep() = viewModel.getDataCep(cepValue)

    LaunchedEffect(viewStateGetDataCep) {
        when (viewStateGetDataCep) {
            is State.Success -> {
                if ((viewStateGetDataCep as State.Success<CepViewData>).data.error == true) {
                    val (titleResId, messageResId) =
                        viewModel.getErrorDialogResources(InvalidCep)
                    dialogState = DialogState.Error(
                        titleRes = titleResId,
                        messageRes = messageResId,
                        errorType = InvalidCep
                    )
                } else {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }

            is State.Error -> {
                val (titleResId, messageResId) =
                    viewModel.getErrorDialogResources((viewStateGetDataCep as State.Error<CepViewData>).errorType)
                dialogState = DialogState.Error(
                    titleRes = titleResId,
                    messageRes = messageResId,
                    errorType = (viewStateGetDataCep as State.Error<CepViewData>).errorType
                )
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = context.getString(R.string.title_search_cep_top_app_bar))
        }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_16)),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            fieldsList.forEach { field ->
                FormOutlinedTextField(
                    value = field.value,
                    enabled = field.enabled,
                    onValueChange = field.onValueChange,
                    isErrorEmpty = field.isErrorEmpty,
                    isErrorInvalid = field.isErrorInvalid,
                    errorMessageEmpty = context.getString(R.string.error_message_required_field),
                    errorMessageInvalid = context.getString(R.string.error_message_invalid_field),
                    label = field.label,
                    modifier = field.modifier,
                    keyboardType = field.keyboardType,
                    maxLength = field.maxLength
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_20)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.size_16))
            ) {
                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClickBtn = { fetchGetDataCep() },
                    text = context.getString(R.string.text_btn_search_cep),
                    isLoading = isLoading,
                    enabled = !isLoading
                )

                PrimaryButton(
                    modifier = Modifier.weight(1f), onClickBtn = {
                        if (viewStateGetDataCep is State.Success
                            && (viewStateGetDataCep as State.Success<CepViewData>).data.error != true) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                CEP_DATA, (viewStateGetDataCep as State.Success).data
                            )
                            navController.navigate(Routes.DetailsCep.route)
                            viewModel.clearCepStates()
                        } else {
                            dialogState = DialogState.Warning
                        }
                    }, text = context.getString(R.string.text_btn_continue)
                )
            }
        }

        when (val ds = dialogState) {
            is DialogState.Error -> ErrorDialog(
                title = context.getString(ds.titleRes),
                message = context.getString(ds.messageRes),
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    positiveButtonText = R.string.txt_btn_positive_error_dialog,
                    negativeButtonText = R.string.txt_btn_negative_error_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = {
                    dialogState = DialogState.None
                    when (ds.errorType) {
                        NetworkError -> fetchGetDataCep()
                        else -> {
                            viewModel.updateCep("")
                            cepFocusRequester.requestFocus()
                        }
                    }
                },
                onDismissClick = { dialogState = DialogState.None })

            DialogState.Warning -> WarningDialog(
                title = context.getString(R.string.title_continue_error_dialog),
                message = context.getString(R.string.message_continue_error_dialog),
                isCancelable = true,
                dialogButtonProperties = DialogButtonProperties(
                    neutralButtonText = R.string.txt_btn_ok_neutral_dialog,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = White
                ),
                onConfirmClick = { dialogState = DialogState.None },
                onDismissClick = { dialogState = DialogState.None })

            else -> Unit
        }
    }
}
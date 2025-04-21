package com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate

import com.marcelo.souza.buscar.cep.R

sealed interface State<out T> {
    object Initial : State<Nothing>
    object Loading : State<Nothing>
    data class Success<T>(val data: T) : State<T>
    data class Error<T>(val errorType: ErrorType) : State<T>
}

sealed class ErrorType {
    data object Error : ErrorType()
    data object InvalidCep : ErrorType()
    data object EmptyCep : ErrorType()
    data object NetworkError : ErrorType()
}

sealed class DialogState {
    object None : DialogState()
    data class Error(
        val titleRes: Int,
        val messageRes: Int,
        val errorType: ErrorType
    ) : DialogState()

    object Warning : DialogState()
}

fun mapError(type: ErrorType): Pair<Int, Int> = when (type) {
    ErrorType.EmptyCep ->
        R.string.title_get_cep_empty_error_dialog to R.string.message_get_cep_empty_error_dialog

    ErrorType.InvalidCep ->
        R.string.title_get_cep_invalid_error_dialog to R.string.message_get_cep_invalid_error_dialog

    ErrorType.NetworkError ->
        R.string.title_get_cep_network_error_dialog to R.string.message_get_cep_network_error_dialog

    else ->
        R.string.title_get_cep_error_dialog to R.string.message_get_cep_error_dialog
}
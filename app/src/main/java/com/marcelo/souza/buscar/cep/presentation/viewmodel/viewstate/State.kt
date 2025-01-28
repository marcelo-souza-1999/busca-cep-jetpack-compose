package com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate

sealed interface State<T> {
    class Loading<T> : State<T>
    data class Success<T>(val data: T) : State<T>
    open class Error<T>(val errorType: ErrorType) : State<T>
}

sealed class ErrorType {
    data object Error : ErrorType()
    data object NetworkError : ErrorType()
    data object CepInvalidError : ErrorType()
}
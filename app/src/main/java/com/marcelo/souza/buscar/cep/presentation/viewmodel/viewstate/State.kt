package com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate

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
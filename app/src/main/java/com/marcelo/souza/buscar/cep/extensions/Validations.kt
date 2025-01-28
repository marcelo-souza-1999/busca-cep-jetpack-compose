package com.marcelo.souza.buscar.cep.extensions

fun String.isValidCep(): Boolean {
    val cepRegex = Regex("^[0-9]{8}$")
    return this.matches(cepRegex)
}

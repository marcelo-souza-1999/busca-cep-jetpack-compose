package com.marcelo.souza.buscar.cep.domain.model

data class CepViewData(
    val cep: String,
    val street: String,
    val neighborhood: String,
    val city: String,
    val state: String,
    val error: Boolean?
)

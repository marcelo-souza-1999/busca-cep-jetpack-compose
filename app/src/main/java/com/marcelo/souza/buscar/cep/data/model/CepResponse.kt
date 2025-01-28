package com.marcelo.souza.buscar.cep.data.model

import com.google.gson.annotations.SerializedName

data class CepResponse(
    @SerializedName("cep")
    val cep: String,
    @SerializedName("logradouro")
    val street: String,
    @SerializedName("bairro")
    val neighborhood: String,
    @SerializedName("localidade")
    val city: String,
    @SerializedName("estado")
    val state: String,
    @SerializedName("erro")
    val error: Boolean?
)

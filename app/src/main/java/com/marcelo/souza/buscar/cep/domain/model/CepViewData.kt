package com.marcelo.souza.buscar.cep.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CepViewData(
    val cep: String?,
    val street: String?,
    val neighborhood: String?,
    val city: String?,
    val state: String?,
    val error: Boolean?
) : Parcelable
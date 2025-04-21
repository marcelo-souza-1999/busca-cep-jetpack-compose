package com.marcelo.souza.buscar.cep.data.api

import com.marcelo.souza.buscar.cep.data.model.CepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApi {
    @GET(GET_DATA_CEP)
    suspend fun getDataCep(
        @Path(GET_CEP) cep: String
    ): CepResponse

    private companion object {
        const val GET_CEP = "cep"
        const val GET_DATA_CEP = "ws/{cep}/json"
    }
}
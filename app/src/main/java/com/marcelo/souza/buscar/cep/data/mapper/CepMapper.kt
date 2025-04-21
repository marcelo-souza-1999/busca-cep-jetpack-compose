package com.marcelo.souza.buscar.cep.data.mapper

import com.marcelo.souza.buscar.cep.data.model.CepResponse
import com.marcelo.souza.buscar.cep.domain.model.CepViewData

object CepMapper {

    fun mapResponseToViewData(response: CepResponse): CepViewData {
        return CepViewData(
            cep = response.cep,
            street = response.street,
            neighborhood = response.neighborhood,
            city = response.city,
            state = response.state,
            error = response.error
        )
    }
}
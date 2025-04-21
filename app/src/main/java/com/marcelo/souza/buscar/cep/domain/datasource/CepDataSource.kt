package com.marcelo.souza.buscar.cep.domain.datasource

import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import kotlinx.coroutines.flow.Flow

interface CepDataSource {

    fun fetchDataCep(cep: String): Flow<CepViewData>
}
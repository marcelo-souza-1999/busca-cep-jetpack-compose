package com.marcelo.souza.buscar.cep.domain.repository

import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import kotlinx.coroutines.flow.Flow

interface CepRepository {

    suspend fun getDataCep(cep: String): Flow<CepViewData>
}
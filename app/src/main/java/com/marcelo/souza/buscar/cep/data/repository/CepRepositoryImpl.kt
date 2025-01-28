package com.marcelo.souza.buscar.cep.data.repository

import com.marcelo.souza.buscar.cep.domain.datasource.CepDataSource
import com.marcelo.souza.buscar.cep.domain.repository.CepRepository
import org.koin.core.annotation.Single

@Single
class CepRepositoryImpl(
    private val dataSource: CepDataSource
) : CepRepository {

    override suspend fun getDataCep(cep: String) = dataSource.fetchDataCep(cep)
}
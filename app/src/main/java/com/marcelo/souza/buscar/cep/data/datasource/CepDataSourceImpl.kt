package com.marcelo.souza.buscar.cep.data.datasource

import com.marcelo.souza.buscar.cep.data.api.CepApi
import com.marcelo.souza.buscar.cep.data.mapper.CepMapper.mapResponseToViewData
import com.marcelo.souza.buscar.cep.domain.datasource.CepDataSource
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class CepDataSourceImpl(
    private val cepApi: CepApi
) : CepDataSource {

    override fun fetchDataCep(cep: String): Flow<CepViewData> {
        return flow {
            mapResponseToViewData(cepApi.getDataCep(cep))
        }
    }
}
package com.marcelo.souza.buscar.cep.di.modules

import com.marcelo.souza.buscar.cep.data.repository.CepRepositoryImpl
import com.marcelo.souza.buscar.cep.domain.datasource.CepDataSource
import com.marcelo.souza.buscar.cep.domain.repository.CepRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Single
class RepositoryModule {

    fun provideCepRepository(dataSource: CepDataSource): CepRepository =
        CepRepositoryImpl(dataSource)
}
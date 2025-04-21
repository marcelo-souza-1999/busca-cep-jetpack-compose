package com.marcelo.souza.buscar.cep.domain.usecase

import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.repository.CepRepository
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class GetCepUseCase(
    private val cepRepository: CepRepository
) {
    operator fun invoke(cep: String): Flow<State<CepViewData>> =
        cepRepository.getDataCep(cep)
            .map { data -> State.Success(data) }
}
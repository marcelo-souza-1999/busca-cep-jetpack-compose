package com.marcelo.souza.buscar.cep.domain.usecase

import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.repository.CepRepository
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class GetCepUseCase(
    private val cepRepository: CepRepository
) {
    suspend operator fun invoke(cep: String): Flow<State<CepViewData>> = flow {
        try {
            cepRepository.getDataCep(cep).collect { cepViewData ->
                emit(State.Success(cepViewData))
            }
        } catch (exception: Throwable) {
            emit(
                State.Error(
                    errorType = ErrorType.Error
                )
            )
        }
    }
}
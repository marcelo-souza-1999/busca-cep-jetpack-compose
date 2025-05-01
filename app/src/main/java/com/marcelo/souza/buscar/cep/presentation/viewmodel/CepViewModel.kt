package com.marcelo.souza.buscar.cep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.usecase.GetCepUseCase
import com.marcelo.souza.buscar.cep.extensions.isNetworkError
import com.marcelo.souza.buscar.cep.extensions.isValidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.EmptyCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType.InvalidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.mapError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CepViewModel(
    private val getCepUseCase: GetCepUseCase
) : ViewModel() {

    private val _cep = MutableStateFlow("")
    val cep = _cep.asStateFlow()

    private val _street = MutableStateFlow("")
    val street = _street.asStateFlow()

    private val _neighborhood = MutableStateFlow("")
    val neighborhood = _neighborhood.asStateFlow()

    private val _city = MutableStateFlow("")
    val city = _city.asStateFlow()

    private val _state = MutableStateFlow("")
    val state = _state.asStateFlow()

    private val _viewState = MutableStateFlow<State<CepViewData>>(State.Initial)
    var viewState = _viewState.asStateFlow()

    fun getDataCep(cep: String) = viewModelScope.launch {
        when {
            cep.isEmpty() -> {
                _viewState.value = State.Error(EmptyCep)
                return@launch
            }

            !cep.isValidCep() -> {
                _viewState.value = State.Error(InvalidCep)
                return@launch
            }
        }

        getCepUseCase(cep).onStart {
            _viewState.value = State.Loading
        }.catch { exception ->
            val errorType = if (exception.isNetworkError())
                ErrorType.NetworkError else ErrorType.Error
            _viewState.value = State.Error(errorType)
        }.collect { result ->
            if (result is State.Success) {
                _viewState.value = State.Success(result.data)
                if (result.data.error != true) {
                    _cep.value = result.data.cep.orEmpty()
                    _street.value = result.data.street.orEmpty()
                    _neighborhood.value = result.data.neighborhood.orEmpty()
                    _city.value = result.data.city.orEmpty()
                    _state.value = result.data.state.orEmpty()
                }
            }
        }
    }

    fun getErrorDialogResources(type: ErrorType): Pair<Int, Int> = mapError(type)

    fun updateCep(newCep: String) = updateStateValue(newCep, _cep)

    fun updateStreet(newStreet: String) = updateStateValue(newStreet, _street)

    fun updateNeighborhood(newNeighborhood: String) =
        updateStateValue(newNeighborhood, _neighborhood)

    fun updateCity(newCity: String) = updateStateValue(newCity, _city)

    fun updateState(newState: String) = updateStateValue(newState, _state)

    fun clearCepStates() = viewModelScope.launch {
        _cep.value = ""
        _street.value = ""
        _neighborhood.value = ""
        _city.value = ""
        _state.value = ""
        _viewState.value = State.Initial
    }

    private fun updateStateValue(
        newValue: String,
        stateFlow: MutableStateFlow<String>
    ) = viewModelScope.launch {
        stateFlow.value = newValue
    }
}

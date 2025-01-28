package com.marcelo.souza.buscar.cep.presentation.viewmodel

import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.model.FieldsViewData
import com.marcelo.souza.buscar.cep.domain.usecase.GetCepUseCase
import com.marcelo.souza.buscar.cep.extensions.isNetworkError
import com.marcelo.souza.buscar.cep.extensions.isValidCep
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
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

    private val _fieldsState = MutableStateFlow<List<FieldsViewData>>(emptyList())
    val fieldsState = _fieldsState.asStateFlow()

    private val _viewState = MutableStateFlow<State<CepViewData>>(State.Loading())
    val viewState = _viewState.asStateFlow()

    fun getDataCep(cep: String) = viewModelScope.launch {
        getCepUseCase(cep).onStart {
            _viewState.value = State.Loading()
        }.catch { throwable ->
            val errorType = when {
                !cep.isValidCep() -> ErrorType.CepInvalidError
                throwable.isNetworkError() -> ErrorType.NetworkError
                else -> ErrorType.Error
            }
            _viewState.value = State.Error(errorType)
        }.collect { result ->
            if (result is State.Success) {
                _viewState.value = State.Success(result.data)
                _fieldsState.value = mapToFieldsViewData(result.data)
            }
        }
    }

    fun updateCep(newCep: String) = updateStateValue(newCep, _cep)

    fun updateStreet(newStreet: String) = updateStateValue(newStreet, _street)

    fun updateNeighborhood(newNeighborhood: String) =
        updateStateValue(newNeighborhood, _neighborhood)

    fun updateCity(newCity: String) = updateStateValue(newCity, _city)

    fun updateState(newState: String) = updateStateValue(newState, _state)

    fun validateCepField(): Boolean {
        val updatedFields = _fieldsState.value.map { field ->
            if (field.label == LABEL_CEP) {
                val isEmpty = field.value.isEmpty()
                val isInvalid = !field.value.isValidCep()

                field.copy(
                    isErrorEmpty = isEmpty,
                    isErrorInvalid = isInvalid
                )
            } else {
                field
            }
        }

        _fieldsState.value = updatedFields

        return updatedFields.find { it.label == LABEL_CEP }?.let {
            !it.isErrorEmpty && !it.isErrorInvalid
        } ?: false
    }


    private fun updateStateValue(newValue: String, stateFlow: MutableStateFlow<String>) =
        viewModelScope.launch {
            stateFlow.value = newValue
        }

    private fun mapToFieldsViewData(cepViewData: CepViewData): List<FieldsViewData> {
        return listOf(
            createFieldViewData(
                value = cepViewData.cep,
                onValueChange = ::updateCep,
                label = LABEL_CEP,
                enabled = true,
                keyboardType = KeyboardType.Number,
                maxLength = EIGHT
            ),
            createFieldViewData(
                value = cepViewData.street,
                onValueChange = ::updateStreet,
                label = LABEL_STREET,
                enabled = false,
                keyboardType = KeyboardType.Text
            ),
            createFieldViewData(
                value = cepViewData.neighborhood,
                onValueChange = ::updateNeighborhood,
                label = LABEL_NEIGHBORHOOD,
                enabled = false,
                keyboardType = KeyboardType.Text
            ),
            createFieldViewData(
                value = cepViewData.city,
                onValueChange = ::updateCity,
                label = LABEL_CITY,
                enabled = false,
                keyboardType = KeyboardType.Text
            ),
            createFieldViewData(
                value = cepViewData.state,
                onValueChange = ::updateState,
                label = LABEL_STATE,
                enabled = false,
                keyboardType = KeyboardType.Text
            )
        )
    }

    private fun createFieldViewData(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        enabled: Boolean,
        keyboardType: KeyboardType,
        isErrorEmpty: Boolean = false,
        isErrorInvalid: Boolean = false,
        maxLength: Int? = null
    ): FieldsViewData {
        return FieldsViewData(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = label,
            keyboardType = keyboardType,
            isErrorEmpty = isErrorEmpty,
            isErrorInvalid = isErrorInvalid,
            maxLength = maxLength
        )
    }

    private companion object {
        private const val LABEL_CEP = "Cep"
        private const val LABEL_STREET = "Rua"
        private const val LABEL_NEIGHBORHOOD = "Bairro"
        private const val LABEL_CITY = "Cidade"
        private const val LABEL_STATE = "Estado"
        private const val EIGHT = 8
    }
}

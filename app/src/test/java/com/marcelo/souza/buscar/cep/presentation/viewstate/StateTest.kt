package com.marcelo.souza.buscar.cep.presentation.viewstate

import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.mapError
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

class StateTest {

    @Test
    fun `Initial should be of type State and equals itself`() {
        val state = State.Initial
        assertTrue(true)
        assertEquals(State.Initial, state)
    }

    @Test
    fun `Loading should be of type State and equals itself`() {
        val state = State.Loading
        assertTrue(true)
        assertEquals(State.Loading, state)
    }

    @Test
    fun `Success should hold the correct data`() {
        val data = "some result"
        val state: State<String> = State.Success(data)

        assertTrue(true)
        assertEquals(data, (state as State.Success).data)
    }

    @Test
    fun `Success states with same data should be equal`() {
        val data = "same data"
        val state1 = State.Success(data)
        val state2 = State.Success(data)

        assertEquals(state1, state2)
    }

    @Test
    fun `Error should hold the correct error type`() {
        val errorType = ErrorType.InvalidCep
        val state: State<String> = State.Error(errorType)

        assertTrue(true)
        assertEquals(errorType, (state as State.Error).errorType)
    }

    @Test
    fun `Error states with same error type should be equal`() {
        val errorType = ErrorType.NetworkError
        val state1 = State.Error<String>(errorType)
        val state2 = State.Error<String>(errorType)

        assertEquals(state1, state2)
    }

    @Test
    fun mapError_withEmptyCep_shouldReturnCorrectTitleAndMessage() {
        val result = mapError(ErrorType.EmptyCep)
        val expected = R.string.title_get_cep_empty_error_dialog to R.string.message_get_cep_empty_error_dialog
        assertEquals(expected, result)
    }

    @Test
    fun mapError_withInvalidCep_shouldReturnCorrectTitleAndMessage() {
        val result = mapError(ErrorType.InvalidCep)
        val expected = R.string.title_get_cep_invalid_error_dialog to R.string.message_get_cep_invalid_error_dialog
        assertEquals(expected, result)
    }

    @Test
    fun mapError_withNetworkError_shouldReturnCorrectTitleAndMessage() {
        val result = mapError(ErrorType.NetworkError)
        val expected = R.string.title_get_cep_network_error_dialog to R.string.message_get_cep_network_error_dialog
        assertEquals(expected, result)
    }

    @Test
    fun mapError_withGenericError_shouldReturnCorrectTitleAndMessage() {
        val result = mapError(ErrorType.Error)
        val expected = R.string.title_get_cep_error_dialog to R.string.message_get_cep_error_dialog
        assertEquals(expected, result)
    }
}
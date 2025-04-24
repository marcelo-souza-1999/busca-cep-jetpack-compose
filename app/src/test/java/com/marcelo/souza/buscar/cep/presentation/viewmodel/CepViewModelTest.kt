package com.marcelo.souza.buscar.cep.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.marcelo.souza.buscar.cep.R
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.usecase.GetCepUseCase
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.ErrorType
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class CepViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var getCepUseCase: GetCepUseCase

    private lateinit var viewModel: CepViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = CepViewModel(getCepUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun getErrorDialogResources_shouldReturnCorrectResources() {
        val empty = viewModel.getErrorDialogResources(ErrorType.EmptyCep)
        assertEquals(R.string.title_get_cep_empty_error_dialog, empty.first)
        assertEquals(R.string.message_get_cep_empty_error_dialog, empty.second)

        val invalid = viewModel.getErrorDialogResources(ErrorType.InvalidCep)
        assertEquals(R.string.title_get_cep_invalid_error_dialog, invalid.first)
        assertEquals(R.string.message_get_cep_invalid_error_dialog, invalid.second)

        val network = viewModel.getErrorDialogResources(ErrorType.NetworkError)
        assertEquals(R.string.title_get_cep_network_error_dialog, network.first)
        assertEquals(R.string.message_get_cep_network_error_dialog, network.second)

        val generic = viewModel.getErrorDialogResources(ErrorType.Error)
        assertEquals(R.string.title_get_cep_error_dialog, generic.first)
        assertEquals(R.string.message_get_cep_error_dialog, generic.second)
    }

    @Test
    fun getDataCep_emptyInput_shouldEmitEmptyCepError() = runTest(testDispatcher) {
        viewModel.getDataCep("")
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.viewState.value
        assertTrue(state is State.Error)
        assertEquals(ErrorType.EmptyCep, (state as State.Error).errorType)
    }

    @Test
    fun getDataCep_invalidLength_shouldEmitInvalidCepError() = runTest(testDispatcher) {
        viewModel.getDataCep("123")
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.viewState.value
        assertTrue(state is State.Error)
        assertEquals(ErrorType.InvalidCep, (state as State.Error).errorType)
    }

    @Test
    fun getDataCep_validCep_shouldUpdateFieldsAndEmitSuccessState() = runTest(testDispatcher) {
        val dummy = CepViewData(
            cep = "01001000",
            street = "Rua X",
            neighborhood = "Bairro Y",
            city = "Cidade Z",
            state = "ST",
            error = false
        )
        coEvery { getCepUseCase("01001000") } returns flowOf(State.Success(dummy))

        viewModel.getDataCep("01001000")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is State.Success)
        assertEquals(dummy, (state as State.Success).data)
        assertEquals(dummy.cep, viewModel.cep.value)
        assertEquals(dummy.street, viewModel.street.value)
        assertEquals(dummy.neighborhood, viewModel.neighborhood.value)
        assertEquals(dummy.city, viewModel.city.value)
        assertEquals(dummy.state, viewModel.state.value)
    }

    @Test
    fun getDataCep_responseWithErrorTrue_shouldNotUpdateFields() = runTest(testDispatcher) {
        val errorData = CepViewData(
            cep = null,
            street = null,
            neighborhood = null,
            city = null,
            state = null,
            error = true
        )
        coEvery { getCepUseCase(any()) } returns flowOf(State.Success(errorData))

        viewModel.getDataCep("00000000")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("", viewModel.cep.value)
        assertEquals("", viewModel.street.value)
        assertEquals("", viewModel.neighborhood.value)
        assertEquals("", viewModel.city.value)
        assertEquals("", viewModel.state.value)
    }

    @Test
    fun getDataCep_networkException_shouldEmitNetworkError() = runTest(testDispatcher) {
        coEvery { getCepUseCase(any()) } returns flow {
            throw UnknownHostException()
        }

        viewModel.getDataCep("01001000")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is State.Error)
        assertEquals(ErrorType.NetworkError, (state as State.Error).errorType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateFields_shouldUpdateRespectiveValues() = runTest(testDispatcher) {
        viewModel.updateCep("A")
        runCurrent()
        assertEquals("A", viewModel.cep.value)
        viewModel.updateStreet("B")
        runCurrent()
        assertEquals("B", viewModel.street.value)
        viewModel.updateNeighborhood("C")
        runCurrent()
        assertEquals("C", viewModel.neighborhood.value)
        viewModel.updateCity("D")
        runCurrent()
        assertEquals("D", viewModel.city.value)
        viewModel.updateState("E")
        runCurrent()
        assertEquals("E", viewModel.state.value)
    }

    @Test
    fun clearCepStates_shouldResetFieldsAndEmitInitialState() = runTest(testDispatcher) {
        viewModel.updateCep("X")
        viewModel.updateStreet("Y")
        viewModel.updateNeighborhood("Z")
        viewModel.updateCity("W")
        viewModel.updateState("V")

        viewModel.clearCepStates()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("", viewModel.cep.value)
        assertEquals("", viewModel.street.value)
        assertEquals("", viewModel.neighborhood.value)
        assertEquals("", viewModel.city.value)
        assertEquals("", viewModel.state.value)
        assertTrue(viewModel.viewState.value is State.Initial)
    }

    @Test
    fun getDataCep_unexpectedException_shouldEmitGenericError() = runTest(testDispatcher) {
        coEvery { getCepUseCase(any()) } returns flow { throw IllegalStateException("boom") }

        viewModel.getDataCep("01001000")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is State.Error)
        assertEquals(ErrorType.Error, (state as State.Error).errorType)
    }

    @Test
    fun getDataCep_invalidCharacters_shouldEmitInvalidCepError() = runTest(testDispatcher) {
        viewModel.getDataCep("ABCDE-123")
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.viewState.value
        assertTrue(state is State.Error)
        assertEquals(ErrorType.InvalidCep, (state as State.Error).errorType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateCep_shouldNotAffectViewState() = runTest(testDispatcher) {
        viewModel.updateCep("12345678")
        runCurrent()
        assertTrue(viewModel.viewState.value is State.Initial)
    }

    @Test
    fun clearCepStates_afterError_shouldEmitInitialState() = runTest(testDispatcher) {
        viewModel.getDataCep("")
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.viewState.value is State.Error)

        viewModel.clearCepStates()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.viewState.value is State.Initial)
    }

    @Test
    fun getErrorDialogResources_shouldMapCorrectTitleForEachErrorType() {
        listOf(
            ErrorType.EmptyCep to R.string.title_get_cep_empty_error_dialog,
            ErrorType.InvalidCep to R.string.title_get_cep_invalid_error_dialog,
            ErrorType.NetworkError to R.string.title_get_cep_network_error_dialog,
            ErrorType.Error to R.string.title_get_cep_error_dialog
        ).forEach { (type, expectedTitle) ->
            val (title, _) = viewModel.getErrorDialogResources(type)
            assertEquals(expectedTitle, title)
        }
    }
}

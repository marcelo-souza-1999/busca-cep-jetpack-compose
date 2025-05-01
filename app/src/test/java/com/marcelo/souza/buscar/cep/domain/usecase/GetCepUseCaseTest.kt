package com.marcelo.souza.buscar.cep.domain.usecase

import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import com.marcelo.souza.buscar.cep.domain.repository.CepRepository
import com.marcelo.souza.buscar.cep.presentation.viewmodel.viewstate.State
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class GetCepUseCaseTest {

    @MockK
    private lateinit var cepRepository: CepRepository

    private lateinit var getCepUseCase: GetCepUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getCepUseCase = GetCepUseCase(cepRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should call repository with given cep`() = runTest {
        val testCep = "12345678"
        val dummyData = CepViewData(
            cep = testCep,
            street = "Street",
            neighborhood = "Neighborhood",
            city = "City",
            state = "ST",
            error = false
        )
        coEvery { cepRepository.getDataCep(testCep) } returns flowOf(dummyData)

        getCepUseCase(testCep).first()

        coVerify(exactly = 1) { cepRepository.getDataCep(testCep) }
    }

    @Test
    fun `invoke should map repository data to StateSuccess`() = runTest {
        val testCep = "87654321"
        val dummyData = CepViewData(
            cep = testCep,
            street = "Avenida",
            neighborhood = "Bairro",
            city = "Local",
            state = "SP",
            error = false
        )
        coEvery { cepRepository.getDataCep(testCep) } returns flowOf(dummyData)

        val states = mutableListOf<State<CepViewData>>()
        getCepUseCase(testCep).collect { state ->
            states.add(state)
        }

        assertEquals(1, states.size)
        val firstState = states[0]
        assertTrue(firstState is State.Success)
        assertEquals(dummyData, (firstState as State.Success).data)
    }

    @Test
    fun `invoke should map multiple emissions correctly`() = runTest {
        val testCep = "11223344"
        val data1 = CepViewData(
            cep = testCep,
            street = "Rua1",
            neighborhood = "Bairro1",
            city = "Cidade1",
            state = "RJ",
            error = false
        )
        val data2 = CepViewData(
            cep = testCep,
            street = "Rua2",
            neighborhood = "Bairro2",
            city = "Cidade2",
            state = "MG",
            error = false
        )
        coEvery { cepRepository.getDataCep(testCep) } returns flowOf(data1, data2)

        val states = getCepUseCase(testCep).toList()

        assertEquals(2, states.size)
        assertEquals(data1, (states[0] as State.Success).data)
        assertEquals(data2, (states[1] as State.Success).data)
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        val testCep = "00000000"
        val exception = RuntimeException("Repo error")
        coEvery { cepRepository.getDataCep(testCep) } returns flow { throw exception }

        try {
            getCepUseCase(testCep).first()
            fail("Expected RuntimeException")
        } catch (e: RuntimeException) {
            assertEquals("Repo error", e.message)
        }
    }
}
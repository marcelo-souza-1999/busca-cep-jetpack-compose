package com.marcelo.souza.buscar.cep.data.repository

import com.marcelo.souza.buscar.cep.domain.datasource.CepDataSource
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class CepRepositoryImplTest {

    @MockK
    private lateinit var dataSource: CepDataSource

    private lateinit var repository: CepRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = CepRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getDataCep should delegate to dataSource with correct cep`() = runTest {
        val testCep = "12345678"
        val dummyData = sampleViewData(testCep)
        coEvery { dataSource.fetchDataCep(testCep) } returns flowOf(dummyData)

        repository.getDataCep(testCep).first()

        coVerify(exactly = 1) { dataSource.fetchDataCep(testCep) }
    }

    @Test
    fun `getDataCep should return same emissions as dataSource`() = runTest {
        val testCep = "87654321"
        val d1 = sampleViewData(testCep).copy(street = "S1")
        val d2 = sampleViewData(testCep).copy(street = "S2")
        coEvery { dataSource.fetchDataCep(testCep) } returns flowOf(d1, d2)

        val emissions = repository.getDataCep(testCep).toList()

        assertEquals(2, emissions.size)
        assertEquals(d1, emissions[0])
        assertEquals(d2, emissions[1])
    }

    @Test
    fun `getDataCep should propagate exceptions from dataSource`() = runTest {
        val testCep = "00000000"
        val exception = IllegalStateException("source failure")
        coEvery { dataSource.fetchDataCep(testCep) } returns flow { throw exception }

        try {
            repository.getDataCep(testCep).first()
            fail("Expected exception to be thrown")
        } catch (e: IllegalStateException) {
            assertEquals("source failure", e.message)
        }
    }

    private fun sampleViewData(cep: String) = CepViewData(
        cep = cep,
        street = "Rua",
        neighborhood = "Bairro",
        city = "Cidade",
        state = "ST",
        error = false
    )
}
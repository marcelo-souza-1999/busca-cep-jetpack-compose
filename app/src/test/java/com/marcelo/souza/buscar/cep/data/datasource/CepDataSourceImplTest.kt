package com.marcelo.souza.buscar.cep.data.datasource

import com.marcelo.souza.buscar.cep.data.api.CepApi
import com.marcelo.souza.buscar.cep.data.model.CepResponse
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class CepDataSourceImplTest {

    @MockK
    private lateinit var api: CepApi

    private lateinit var dataSourceImpl: CepDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataSourceImpl = CepDataSourceImpl(api)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetchDataCep should call api and emit mapped view data`() = runTest {
        val testCep = "01001000"
        val response = CepResponse(
            cep = testCep,
            street = "Rua Teste",
            neighborhood = "Bairro Teste",
            city = "Cidade Teste",
            state = "ST",
            error = false
        )
        coEvery { api.getDataCep(testCep) } returns response

        val viewData: CepViewData = dataSourceImpl.fetchDataCep(testCep).first()
        coVerify(exactly = 1) { api.getDataCep(testCep) }

        assertEquals(testCep, viewData.cep)
        assertEquals("Rua Teste", viewData.street)
        assertEquals("Bairro Teste", viewData.neighborhood)
        assertEquals("Cidade Teste", viewData.city)
        assertEquals("ST", viewData.state)
        viewData.error?.let { assertTrue(!it) }
    }

    @Test
    fun `fetchDataCep should emit only one item`() = runTest {
        val testCep = "22222222"
        val response = CepResponse(
            cep = testCep,
            street = "Rua 1",
            neighborhood = "Bairro 1",
            city = "Cidade 1",
            state = "ES",
            error = false
        )
        coEvery { api.getDataCep(testCep) } returns response

        val emissions = dataSourceImpl.fetchDataCep(testCep).toList()
        assertEquals(1, emissions.size)
    }

    @Test
    fun `fetchDataCep should propagate exception from api`() = runTest {
        val testCep = "00000000"
        val exception = RuntimeException("API failure")
        coEvery { api.getDataCep(testCep) } throws exception

        try {
            dataSourceImpl.fetchDataCep(testCep).first()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("API failure", e.message)
        }
    }
}
package com.marcelo.souza.buscar.cep.data.mapper

import com.marcelo.souza.buscar.cep.data.model.CepResponse
import com.marcelo.souza.buscar.cep.domain.model.CepViewData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CepMapperTest {

    @Test
    fun `mapResponseToViewData should map all fields correctly when error false`() {
        val response = CepResponse(
            cep = "12345678",
            street = "Rua A",
            neighborhood = "Bairro B",
            city = "Cidade C",
            state = "ST",
            error = false
        )

        val viewData: CepViewData = CepMapper.mapResponseToViewData(response)

        assertEquals("12345678", viewData.cep)
        assertEquals("Rua A", viewData.street)
        assertEquals("Bairro B", viewData.neighborhood)
        assertEquals("Cidade C", viewData.city)
        assertEquals("ST", viewData.state)
        assertEquals(false, viewData.error)
    }

    @Test
    fun `mapResponseToViewData should map error true correctly`() {
        val response = CepResponse(
            cep = "87654321",
            street = "Rua X",
            neighborhood = "Bairro Y",
            city = "Cidade Z",
            state = "SP",
            error = true
        )

        val viewData: CepViewData = CepMapper.mapResponseToViewData(response)

        assertEquals(true, viewData.error)
    }

    @Test
    fun `mapResponseToViewData should map null error field`() {
        val response = CepResponse(
            cep = "99999999",
            street = "Rua A",
            neighborhood = "Bairro B",
            city = "Cidade C",
            state = "ST",
            error = null
        )

        val viewData: CepViewData = CepMapper.mapResponseToViewData(response)

        assertEquals("99999999", viewData.cep)
        assertEquals("Rua A", viewData.street)
        assertEquals("Bairro B", viewData.neighborhood)
        assertEquals("Cidade C", viewData.city)
        assertEquals("ST", viewData.state)
        assertNull(viewData.error)
    }
}

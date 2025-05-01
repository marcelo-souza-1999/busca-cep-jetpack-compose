package com.marcelo.souza.buscar.cep.data.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import org.junit.Test

class CepResponseTest {

    @Test
    fun `constructor should assign all provided values`() {
        val response = CepResponse(
            cep = "12345678",
            street = "Rua A",
            neighborhood = "Bairro B",
            city = "Cidade C",
            state = "ST",
            error = true
        )

        assertEquals("12345678", response.cep)
        assertEquals("Rua A", response.street)
        assertEquals("Bairro B", response.neighborhood)
        assertEquals("Cidade C", response.city)
        assertEquals("ST", response.state)
        assertTrue(response.error == true)
    }

    @Test
    fun `default error parameter should be false`() {
        val response = CepResponse(
            cep = "87654321",
            street = "Rua X",
            neighborhood = "Bairro Y",
            city = "Cidade Z",
            state = "SP"
        )

        assertFalse(response.error == true)
    }

    @Test
    fun `equals and hashCode should work correctly`() {
        val r1 = CepResponse(
            cep = "11111111",
            street = "Rua",
            neighborhood = "Bairro",
            city = "Cidade",
            state = "ST",
            error = false
        )
        val r2 = CepResponse(
            cep = "11111111",
            street = "Rua",
            neighborhood = "Bairro",
            city = "Cidade",
            state = "ST",
            error = false
        )

        assertEquals(r1, r2)
        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun `copy should create a new instance with modified fields`() {
        val original = CepResponse(
            cep = "22222222",
            street = "OldStreet",
            neighborhood = "OldNeighborhood",
            city = "OldCity",
            state = "OS",
            error = false
        )
        val copy = original.copy(
            street = "NewStreet",
            error = true
        )

        assertNotSame(original, copy)
        assertEquals("NewStreet", copy.street)
        assertTrue(copy.error == true)
        assertEquals(original.cep, copy.cep)
        assertEquals(original.neighborhood, copy.neighborhood)
        assertEquals(original.city, copy.city)
        assertEquals(original.state, copy.state)
    }

    @Test
    fun `toString should contain all properties`() {
        val response = CepResponse(
            cep = "33333333",
            street = "S",
            neighborhood = "N",
            city = "C",
            state = "ST",
            error = false
        )
        val text = response.toString()

        assertTrue(text.contains("cep=33333333"))
        assertTrue(text.contains("street=S"))
        assertTrue(text.contains("neighborhood=N"))
        assertTrue(text.contains("city=C"))
        assertTrue(text.contains("state=ST"))
        assertTrue(text.contains("error=false"))
    }
}

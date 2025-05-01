package com.marcelo.souza.buscar.cep.extensions

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ValidationCep {

    @Test
    fun `should return true for valid CEP with 8 digits`() {
        val validCep = "12345678"
        assertTrue(validCep.isValidCep())
    }

    @Test
    fun `should return false for CEP with letters`() {
        val invalidCep = "12ab5678"
        assertFalse(invalidCep.isValidCep())
    }

    @Test
    fun `should return false for CEP with less than 8 digits`() {
        val shortCep = "1234567"
        assertFalse(shortCep.isValidCep())
    }

    @Test
    fun `should return false for CEP with more than 8 digits`() {
        val longCep = "123456789"
        assertFalse(longCep.isValidCep())
    }

    @Test
    fun `should return false for empty string`() {
        val emptyCep = ""
        assertFalse(emptyCep.isValidCep())
    }

    @Test
    fun `should return false for CEP with special characters`() {
        val specialCharCep = "12345-67"
        assertFalse(specialCharCep.isValidCep())
    }
}
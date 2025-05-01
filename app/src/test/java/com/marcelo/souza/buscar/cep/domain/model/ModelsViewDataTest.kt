package com.marcelo.souza.buscar.cep.domain.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ModelsViewDataTest {

    @Test
    fun `when creating CepViewData with valid values, should store values correctly`() {
        val cep = "12345-678"
        val street = "Rua das Flores"
        val neighborhood = "Centro"
        val city = "São Paulo"
        val state = "SP"
        val error = false

        val cepViewData = CepViewData(
            cep = cep,
            street = street,
            neighborhood = neighborhood,
            city = city,
            state = state,
            error = error
        )

        assertEquals(cep, cepViewData.cep)
        assertEquals(street, cepViewData.street)
        assertEquals(neighborhood, cepViewData.neighborhood)
        assertEquals(city, cepViewData.city)
        assertEquals(state, cepViewData.state)
        assertEquals(error, cepViewData.error)
    }

    @Test
    fun `when creating CepViewData with null values, should store null values correctly`() {
        val cepViewData = CepViewData(
            cep = null,
            street = null,
            neighborhood = null,
            city = null,
            state = null,
            error = null
        )

        assertNull(cepViewData.cep)
        assertNull(cepViewData.street)
        assertNull(cepViewData.neighborhood)
        assertNull(cepViewData.city)
        assertNull(cepViewData.state)
        assertNull(cepViewData.error)
    }

    @Test
    fun `when comparing two equal CepViewData objects, should return true`() {
        val cepViewData1 =
            CepViewData("12345-678", "Rua das Flores", "Centro", "São Paulo", "SP", false)
        val cepViewData2 =
            CepViewData("12345-678", "Rua das Flores", "Centro", "São Paulo", "SP", false)

        assertEquals(cepViewData1, cepViewData2)
    }

    @Test
    fun `when comparing two different CepViewData objects, should return false`() {
        val cepViewData1 =
            CepViewData("12345-678", "Rua das Flores", "Centro", "São Paulo", "SP", false)
        val cepViewData2 =
            CepViewData("87654-321", "Avenida Brasil", "Jardim", "Rio de Janeiro", "RJ", true)

        assertNotEquals(cepViewData1, cepViewData2)
    }

    @Test
    fun `when copying CepViewData with changed values, should return new object with updated values`() {
        val original =
            CepViewData("12345-678", "Rua das Flores", "Centro", "São Paulo", "SP", false)

        val copy = original.copy(city = "Campinas", state = "SP")

        assertEquals("12345-678", copy.cep)
        assertEquals("Rua das Flores", copy.street)
        assertEquals("Centro", copy.neighborhood)
        assertEquals("Campinas", copy.city)
        assertEquals("SP", copy.state)
        assertEquals(false, copy.error)
        assertNotEquals(original, copy)
    }

    @Test
    fun `when creating FieldsViewData with default values, should store values correctly`() {
        val value = "Test"
        val label = "Test Label"
        val keyboardType = KeyboardType.Text
        val onValueChange: (String) -> Unit = {}

        val fieldsViewData = FieldsViewData(
            value = value,
            label = label,
            onValueChange = onValueChange,
            keyboardType = keyboardType
        )

        assertEquals(value, fieldsViewData.value)
        assertEquals(label, fieldsViewData.label)
        assertEquals(false, fieldsViewData.isErrorEmpty)
        assertEquals(false, fieldsViewData.isErrorInvalid)
        assertEquals(true, fieldsViewData.enabled)
        assertEquals(Modifier, fieldsViewData.modifier)
        assertEquals(keyboardType, fieldsViewData.keyboardType)
        assertNull(fieldsViewData.maxLength)
    }

    @Test
    fun `when creating FieldsViewData with custom values, should store values correctly`() {
        val value = "Test"
        val label = "Test Label"
        val isErrorEmpty = true
        val isErrorInvalid = true
        val enabled = false
        val modifier = Modifier.fillMaxWidth()
        val keyboardType = KeyboardType.Number
        val maxLength = 10
        val onValueChange: (String) -> Unit = {}

        val fieldsViewData = FieldsViewData(
            value = value,
            label = label,
            isErrorEmpty = isErrorEmpty,
            isErrorInvalid = isErrorInvalid,
            enabled = enabled,
            modifier = modifier,
            onValueChange = onValueChange,
            keyboardType = keyboardType,
            maxLength = maxLength
        )

        assertEquals(value, fieldsViewData.value)
        assertEquals(label, fieldsViewData.label)
        assertEquals(isErrorEmpty, fieldsViewData.isErrorEmpty)
        assertEquals(isErrorInvalid, fieldsViewData.isErrorInvalid)
        assertEquals(enabled, fieldsViewData.enabled)
        assertEquals(modifier, fieldsViewData.modifier)
        assertEquals(keyboardType, fieldsViewData.keyboardType)
        assertEquals(maxLength, fieldsViewData.maxLength)
    }

    @Test
    fun `when comparing two equal FieldsViewData objects, should return true`() {
        val onValueChange: (String) -> Unit = {}
        val fieldsViewData1 = FieldsViewData(
            value = "Test",
            label = "Label",
            keyboardType = KeyboardType.Text,
            onValueChange = onValueChange
        )
        val fieldsViewData2 = FieldsViewData(
            value = "Test",
            label = "Label",
            keyboardType = KeyboardType.Text,
            onValueChange = onValueChange
        )

        assertEquals(fieldsViewData1.value, fieldsViewData2.value)
        assertEquals(fieldsViewData1.label, fieldsViewData2.label)
        assertEquals(fieldsViewData1.isErrorEmpty, fieldsViewData2.isErrorEmpty)
        assertEquals(fieldsViewData1.isErrorInvalid, fieldsViewData2.isErrorInvalid)
        assertEquals(fieldsViewData1.enabled, fieldsViewData2.enabled)
        assertEquals(fieldsViewData1.keyboardType, fieldsViewData2.keyboardType)
        assertEquals(fieldsViewData1.maxLength, fieldsViewData2.maxLength)
    }

    @Test
    fun `when copying FieldsViewData with changed values, should return new object with updated values`() {
        val onValueChange: (String) -> Unit = {}
        val original = FieldsViewData(
            value = "Test",
            label = "Label",
            keyboardType = KeyboardType.Text,
            onValueChange = onValueChange
        )

        val copy = original.copy(value = "New Value", isErrorEmpty = true)

        assertEquals("New Value", copy.value)
        assertEquals("Label", copy.label)
        assertEquals(true, copy.isErrorEmpty)
        assertEquals(false, copy.isErrorInvalid)
        assertEquals(KeyboardType.Text, copy.keyboardType)
    }
}
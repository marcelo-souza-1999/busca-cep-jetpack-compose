package com.marcelo.souza.buscar.cep.data.api

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CepApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: CepApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CepApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getDataCep should request correct endpoint and parse response`() = runBlocking {
        val mockJson = """
            {
              "cep": "01001-000",
              "logradouro": "Praça da Sé",
              "bairro": "Sé",
              "localidade": "São Paulo",
              "estado": "SP",
              "erro": false
            }
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setBody(mockJson)
                .setResponseCode(200)
        )

        val response = api.getDataCep("01001000")

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/ws/01001000/json", request.path)
        assertEquals("01001-000", response.cep)
        assertEquals("Praça da Sé", response.street)
        assertEquals("Sé", response.neighborhood)
        assertEquals("São Paulo", response.city)
        assertEquals("SP", response.state)
        assertEquals(false, response.error)
    }
}
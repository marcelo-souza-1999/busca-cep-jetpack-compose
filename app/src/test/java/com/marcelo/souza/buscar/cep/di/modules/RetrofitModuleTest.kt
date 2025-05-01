package com.marcelo.souza.buscar.cep.di.modules

import com.marcelo.souza.buscar.cep.data.api.CepApi
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.ksp.generated.com_marcelo_souza_buscar_cep_di_modules_RetrofitModule as retrofitModule

class RetrofitModuleTest : KoinComponent {

    private val gsonFactory: GsonConverterFactory by inject()
    private val cepApi: CepApi by inject()

    @Before
    fun setUp() {
        startKoin {
            modules(retrofitModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `provideGsonConvertFactory should return singleton GsonConverterFactory`() {
        assertNotNull(gsonFactory)
        val another: GsonConverterFactory by inject()
        assertSame(gsonFactory, another)
    }

    @Test
    fun `provideRetrofit should return singleton CepApi`() {
        assertNotNull(cepApi)
        val anotherApi: CepApi by inject()
        assertSame(cepApi, anotherApi)
    }
}
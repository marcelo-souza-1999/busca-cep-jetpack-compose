package com.marcelo.souza.buscar.cep.di.modules

import com.marcelo.souza.buscar.cep.data.api.CepApi
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@Single
class RetrofitModule : KoinComponent {

    @Single
    fun provideGsonConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Single
    fun provideRetrofit(
        converterFactory: GsonConverterFactory
    ): CepApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory).build()
            .create(CepApi::class.java)
    }

    private companion object {
        const val BASE_URL = "https://viacep.com.br/"
    }
}
package com.marcelo.souza.buscar.cep.di.initialization

import android.app.Application
import com.marcelo.souza.buscar.cep.di.modules.RepositoryModule
import com.marcelo.souza.buscar.cep.di.modules.RetrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

class InitializeKoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@InitializeKoinApplication)
            modules(
                defaultModule,
                RetrofitModule().module,
                RepositoryModule().module
            )
        }
    }
}
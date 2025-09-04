package com.comparacarro

import android.app.Application
import com.comparacarro.di.presentationModule
import com.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComparaCarroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@ComparaCarroApplication)
            modules(
                dataModule,
                presentationModule
            )
        }
    }
}

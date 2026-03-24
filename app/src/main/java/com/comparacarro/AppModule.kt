package com.comparacarro

import com.data.di.DataModule
import comparacarro.network.di.NetworkModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
        NetworkModule::class
    ]
)
@ComponentScan
class AppModule

@KoinApplication(modules = [AppModule::class])
class KoinApp

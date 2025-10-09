package com.comparacarro

import com.data.di.DataModule
import com.detail.DetailModule
import com.home.HomeModule
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
        HomeModule::class,
        DetailModule::class
    ]
)
class AppModule

@KoinApplication(modules = [AppModule::class])
class KoinApp

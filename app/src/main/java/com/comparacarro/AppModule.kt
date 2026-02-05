package com.comparacarro

import com.data.di.DataModule
import comparacarro.network.di.NetworkModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

/**
 * AppModule contains only core modules that are always loaded.
 * Feature modules (HomeModule, DetailModule, ComparisonModule, SelectComparisonModule)
 * are loaded dynamically using rememberKoinModules in their respective routes.
 */
@Module(
    includes = [
        DataModule::class,
        NetworkModule::class
    ]
)
@ComponentScan("com.comparacarro.navigation")
class AppModule

@KoinApplication(modules = [AppModule::class])
class KoinApp

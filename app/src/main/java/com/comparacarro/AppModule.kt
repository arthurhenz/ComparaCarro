package com.comparacarro

import com.comparison.ComparisonModule
import com.data.di.DataModule
import com.detail.DetailModule
import com.home.HomeModule
import com.selectCompare.SelectComparisonModule
import comparacarro.network.di.NetworkModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
        NetworkModule::class,
        HomeModule::class,
        DetailModule::class,
        ComparisonModule::class,
        SelectComparisonModule::class
    ]
)
@ComponentScan
class AppModule

@KoinApplication(modules = [AppModule::class])
class KoinApp

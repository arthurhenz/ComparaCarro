package com.comparacarro

import com.comparison.ComparisonModule
import com.selectCompare.SelectComparisonModule
import com.data.di.DataModule
import com.detail.DetailModule
import com.home.HomeModule
import comparacarro.network.di.NetworkModule
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
        HomeModule::class,
        DetailModule::class,
        ComparisonModule::class,
        SelectComparisonModule::class,
        NetworkModule::class
    ]
)
class AppModule

@KoinApplication(modules = [AppModule::class])
class KoinApp

package com.comparacarro

import android.app.Application
import com.common.navigation.Navigator
import com.comparacarro.navigation.AppNavigator
import com.comparacarro.navigation.utils.EntriesProviderAggregator
import com.comparison.CompareScreenScreenProvider
import com.detail.CardDetailScreenProvider
import com.home.HomeScreenProvider
import com.navigation.EntryProvider
import com.selectCompare.SelectComparisonScreenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ksp.generated.module

class ComparaCarroApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ComparaCarroApplication)
            modules(
                AppModule().module,
                navigationModule
            )
        }
    }
}

private val navigationModule =
    module {
        single { AppNavigator() } bind Navigator::class
        single { EntriesProviderAggregator(getAll()) }
        single { HomeScreenProvider() } bind EntryProvider::class
        single { CardDetailScreenProvider() } bind EntryProvider::class
        single { CompareScreenScreenProvider() } bind EntryProvider::class
        single { SelectComparisonScreenProvider() } bind EntryProvider::class
    }

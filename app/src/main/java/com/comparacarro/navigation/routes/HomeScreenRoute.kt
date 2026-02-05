package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.home.HomeModule
import com.home.HomeScreen
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Serializable
data object HomeScreenRoute : NavKey

fun EntryProviderScope<NavKey>.homeScreenRoute(
) {
    entry<HomeScreenRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(HomeModule().module)
        }

        HomeScreen(
            onCardClick = {},
            onCompareFromHome = {}
        )
    }
}

@Single
fun homeScreenProvider() : EntryProviderScope<NavKey>.() -> Unit = { homeScreenRoute() }

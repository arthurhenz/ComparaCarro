package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.comparison.ComparisonModule
import com.comparison.ComparisonScreen
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Serializable
data class CompareScreenRoute(val firstId: String, val secondId: String) : NavKey

fun EntryProviderScope<NavKey>.compareScreenRoute() {
    entry<CompareScreenRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(ComparisonModule().module)
        }

        ComparisonScreen(
            firstId = key.firstId,
            secondId = key.secondId,
        )
    }
}

@Single
fun compareScreenProvider() : EntryProviderScope<NavKey>.() -> Unit = { compareScreenRoute() }

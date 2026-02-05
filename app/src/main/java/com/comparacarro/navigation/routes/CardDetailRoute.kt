package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.detail.DetailModule
import com.detail.DetailScreen
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Serializable
data class CardDetailRoute(val cardId: String) : NavKey

fun EntryProviderScope<NavKey>.cardDetailRoute(
) {
    entry<CardDetailRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(DetailModule().module)
        }

        DetailScreen(
            cardId = key.cardId,
            onBackClick = { },
            onCompareClick = { }
        )
    }
}


@Single
fun cardDetailScreenProvider() : EntryProviderScope<NavKey>.() -> Unit = { cardDetailRoute() }

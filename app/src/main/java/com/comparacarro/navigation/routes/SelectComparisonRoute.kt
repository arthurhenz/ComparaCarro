package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.selectCompare.SelectComparisonModule
import com.selectCompare.SelectComparisonScreen
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module

@Serializable
data class SelectComparisonRoute(val firstId: String?) : NavKey

fun EntryProviderScope<NavKey>.selectComparisonRoute() {

    entry<SelectComparisonRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(SelectComparisonModule().module)
        }

        SelectComparisonScreen(
            firstId = key.firstId,
            onBackClick = { },
            onCardClick = { },
            onCompareClick = { }
        )
    }
}


@Single
fun selectComparisonScreenProvider() : EntryProviderScope<NavKey>.() -> Unit = { selectComparisonRoute() }

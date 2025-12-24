package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.comparison.ComparisonScreen
import kotlinx.serialization.Serializable

@Serializable
data class CompareScreenRoute(val firstId: String, val secondId: String) : NavKey

fun EntryProviderScope<NavKey>.compareScreenRoute(goBack: () -> Unit) {
    entry<CompareScreenRoute> { key ->
        ComparisonScreen(
            firstId = key.firstId,
            secondId = key.secondId,
            onBackClick = goBack
        )
    }
}

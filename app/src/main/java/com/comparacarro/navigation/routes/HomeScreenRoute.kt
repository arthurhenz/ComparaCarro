package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data class HomeScreenRoute(val route: String) : NavKey

fun EntryProviderScope<NavKey>.homeScreenRoute(
    onCardClick: (String) -> Unit,
    onCompareFromHome: () -> Unit
) {
    entry<HomeScreenRoute> { key ->
        HomeScreen(
            onCardClick = onCardClick,
            onCompareFromHome = onCompareFromHome
        )
    }
}

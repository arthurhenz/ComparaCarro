package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.comparacarro.navigation.utils.EntriesProviderAggregator
import com.navigation.routes.HomeScreenRoute
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val navigator = koinInject<AppNavigator>()
    navigator.setStartDestination(HomeScreenRoute)

    val entryProviders = koinInject<EntriesProviderAggregator>().entryProviders
    val entryBuilders = entryProviders.map { it.entryProvider() }

    NavDisplay(
        backStack = navigator.backStack,
        entryProvider = entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        },
    )
}

package com.comparacarro.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
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
        // Give every NavEntry its own ViewModelStore so each route gets a fresh, correctly
        // parameterized ViewModel. Without this, ViewModels fall back to the Activity store and
        // koinViewModel() reuses the first instance (e.g. every card opens the same car).
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entryBuilders.forEach { builder -> this.builder() }
            }
    )
}

package com.comparacarro.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.comparacarro.navigation.utils.EntriesProviderAggregator
import com.navigation.routes.HomeScreenRoute
import com.theme.Theme
import com.ui.BottomNavBar
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val navigator = koinInject<AppNavigator>()
    navigator.setStartDestination(HomeScreenRoute)

    val entryProviders = koinInject<EntriesProviderAggregator>().entryProviders
    val entryBuilders = entryProviders.map { it.entryProvider() }

    // The bottom bar lives once, here, and is derived from the top of the back stack. Screens
    // only lay out their own content; tab routing is centralized in [navigateToBottomTab].
    val selectedTab = navigator.backStack.lastOrNull()?.bottomNavTab()

    Scaffold(
        containerColor = Theme.colors.background,
        // Screens handle status/navigation bar insets themselves; the only padding this
        // Scaffold contributes is the bottom bar height.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (selectedTab != null) {
                BottomNavBar(
                    selected = selectedTab,
                    onSelect = navigator::navigateToBottomTab,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    // The bar already covers the system navigation bar, so screens below it
                    // must not add that inset again.
                    .consumeWindowInsets(innerPadding),
        ) {
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
                    },
            )
        }
    }
}

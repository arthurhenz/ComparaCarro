package com.comparison

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.CompareScreenRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<NavKey>.compareScreenRoute() {
    entry<CompareScreenRoute> { key ->
        val firstSpec = "${key.firstModelSlug},${key.firstFuelAcronym},${key.firstYear}"
        val secondSpec = "${key.secondModelSlug},${key.secondFuelAcronym},${key.secondYear}"
        val viewModel: ComparisonViewModel =
            koinViewModel { parametersOf(ComparisonParams(firstSpec, secondSpec)) }

        val state by viewModel.state.collectAsState()

        ComparisonScreen(
            state = state,
            onBackClick = viewModel::navigateToHome,
            onNavigateToTab = viewModel::navigateToBottomTab,
        )
    }
}

class CompareScreenScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { compareScreenRoute() }
}

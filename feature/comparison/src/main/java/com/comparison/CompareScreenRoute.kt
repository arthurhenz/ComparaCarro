package com.comparison

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

        val state by viewModel.state.collectAsStateWithLifecycle()

        ComparisonScreen(
            state = state,
            onBackClick = viewModel::navigateToHome,
        )
    }
}

class CompareScreenScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { compareScreenRoute() }
}

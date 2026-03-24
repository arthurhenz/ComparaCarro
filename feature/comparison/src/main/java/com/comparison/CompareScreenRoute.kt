package com.comparison

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.navigation.EntryProvider
import com.navigation.routes.CompareScreenRoute
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module

fun EntryProviderScope<NavKey>.compareScreenRoute() {
    entry<CompareScreenRoute> { key ->
        rememberKoinModules(unloadOnForgotten = true, unloadOnAbandoned = true) {
            listOf(ComparisonModule().module)
        }

        val viewModel: ComparisonViewModel = koinViewModel { parametersOf(ComparisonParams(key.firstId, key.secondId)) }

        val state by viewModel.state.collectAsState()

        ComparisonScreen(
            state = state,
            onBackClick = viewModel::navigateToHome
        )
    }
}

class CompareScreenScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { compareScreenRoute() }
}

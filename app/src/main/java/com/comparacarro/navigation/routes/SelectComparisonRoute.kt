package com.comparacarro.navigation.routes

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.selectCompare.SelectComparisonScreen
import kotlinx.serialization.Serializable

@Serializable
data class SelectComparisonRoute(val firstId: String?) : NavKey

fun EntryProviderScope<NavKey>.selectComparisonRoute(
    goBack: () -> Unit,
    onCompareSelected: (firstId: String, secondId: String) -> Unit,
    onCardClick: (String) -> Unit
) {
    entry<SelectComparisonRoute> { key ->
        SelectComparisonScreen(
            firstId = key.firstId,
            onBackClick = goBack,
            onCardClick = onCardClick,
            onCompareClick = { pair ->
                onCompareSelected(pair.first, pair.second)
            }
        )
    }
}

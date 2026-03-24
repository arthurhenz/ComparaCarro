package com.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors

@Composable
fun HomeScreen(
    state: HomeScreenState,
    searchQuery: String,
    isSearchFocused: Boolean,
    sortType: SortType,
    onCardClick: (String) -> Unit = {},
    onCompareFromHome: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit = {},
    onSortTypeChange: (SortType) -> Unit = {},
    onRefreshRecentlyViewed: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    onRefreshRecentlyViewed()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (state) {
        is HomeScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TokenColors.Primary)
            }
        }

        is HomeScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error ?: "Unknown error",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is HomeScreenState.Success -> {
            HomeScreenContent(
                smallCards = state.smallCards,
                recentlyViewedCards = state.recentlyViewedCards,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearchFocusChanged = onSearchFocusChanged,
                isSearchFocused = isSearchFocused,
                sortType = sortType,
                onSortTypeChange = onSortTypeChange,
                onCardClick = onCardClick,
                onCompareClick = onCompareFromHome
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComparaCarrosTheme {
        HomeScreenContent(
            smallCards =
                listOf(
                    SmallCardData(
                        id = "preview_small_1",
                        title = "Volkswagen Saveiro 2017",
                        fipe = "R$ 55.900",
                        selected = true
                    ),
                    SmallCardData(
                        id = "preview_small_2",
                        title = "Audi A4 Quattro Sedan 2019",
                        fipe = "R$ 142.000",
                        selected = false
                    ),
                    SmallCardData(
                        id = "preview_small_3",
                        title = "Honda Civic Si LX LXS 2020",
                        fipe = "R$ 115.500",
                        selected = false
                    ),
                    SmallCardData(
                        id = "preview_small_4",
                        title = "Toyota Corolla Xei Guerra Corolla Siria 2021",
                        fipe = "R$ 128.000",
                        selected = true
                    )
                ),
            recentlyViewedCards =
                listOf(
                    LargeCardData(
                        id = "preview_recent_1",
                        title = "Honda Civic 2020"
                    ),
                    LargeCardData(
                        id = "preview_recent_2",
                        title = "Toyota Corolla 2021"
                    )
                ),
            searchQuery = "",
            onSearchQueryChange = {},
            onSearchFocusChanged = {},
            isSearchFocused = false,
            sortType = SortType.MOST_POPULAR,
            onSortTypeChange = {},
            onCardClick = {}
        )
    }
}

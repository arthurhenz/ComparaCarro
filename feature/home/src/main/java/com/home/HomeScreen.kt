package com.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import com.theme.ComparaCarrosTheme
import com.theme.TokenColors
import com.ui.Header
import com.ui.LargeCard
import com.ui.LargeCardCarousel
import com.ui.PrimaryButton
import com.ui.SmallCard
import com.ui.SmallCardList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onCardClick: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchFocused by viewModel.isSearchFocused.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshRecentlyViewed()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (val currentState = state) {
        is HomeScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.error ?: "Unknown error",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is HomeScreenState.Success -> {
            HomeContent(
                smallCards = currentState.smallCards,
                recentlyViewedCards = currentState.recentlyViewedCards,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onSearchFocusChanged = viewModel::updateSearchFocus,
                isSearchFocused = isSearchFocused,
                sortType = sortType,
                onSortTypeChange = viewModel::updateSortType,
                onCardClick = { cardId ->
                    viewModel.onCardClick(cardId)
                    onCardClick(cardId)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComparaCarrosTheme {
        HomeContent(
            smallCards = listOf(
                SmallCardData(
                    id = "preview_small_1",
                    title = "Volkswagen Saveiro 2017",
                    price = "R$ 55.900",
                    selected = true
                ),
                SmallCardData(
                    id = "preview_small_2",
                    title = "Audi A4 Quattro Sedan 2019",
                    price = "R$ 142.000",
                    selected = false
                ),
                SmallCardData(
                    id = "preview_small_3",
                    title = "Honda Civic Si LX LXS 2020",
                    price = "R$ 115.500",
                    selected = false
                ),
                SmallCardData(
                    id = "preview_small_4",
                    title = "Toyota Corolla Xei Guerra Corolla Siria 2021",
                    price = "R$ 128.000",
                    selected = true
                )
            ),
            recentlyViewedCards = listOf(
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

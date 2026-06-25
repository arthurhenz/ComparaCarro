package com.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.SmallCardData
import com.data.usecase.GetRecentlyViewedCarsUseCase
import com.data.usecase.GetSmallCardsPageUseCase
import com.data.usecase.GetSmallCardsUseCase
import com.data.usecase.SaveRecentlyViewedCarUseCase
import com.navigation.routes.CardDetailRoute
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.navigation.routes.parseVehicleSpec
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

enum class SortType {
    MOST_POPULAR,
    ALPHABETIC,
}

@KoinViewModel
class HomeViewModel(
    private val getSmallCardsUseCase: GetSmallCardsUseCase,
    private val getSmallCardsPageUseCase: GetSmallCardsPageUseCase,
    private val getRecentlyViewedCarsUseCase: GetRecentlyViewedCarsUseCase,
    private val saveRecentlyViewedCarUseCase: SaveRecentlyViewedCarUseCase,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchFocused = MutableStateFlow(false)
    val isSearchFocused: StateFlow<Boolean> = _isSearchFocused.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.MOST_POPULAR)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    // The initial (browse) page cached so we can restore it instantly when the search is cleared.
    private var browseCards: List<SmallCardData> = emptyList()

    // Debounce handle so each keystroke cancels the previous pending FIPE request.
    private var searchJob: Job? = null

    init {
        loadCards()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.length < MIN_SEARCH_LENGTH) {
            // Below the threshold (including empty) shows the cached browse list immediately.
            restoreBrowseCards()
            return
        }
        searchJob =
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                performSearch(trimmed)
            }
    }

    fun updateSearchFocus(isFocused: Boolean) {
        _isSearchFocused.value = isFocused
    }

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
        val current = _state.value
        if (current is HomeScreenState.Success) {
            _state.value = current.copy(smallCards = applySorting(current.allSmallCards))
        }
    }

    private suspend fun performSearch(query: String) {
        val current = _state.value
        if (current !is HomeScreenState.Success) return
        _state.value = current.copy(isSearching = true)
        try {
            val result = getSmallCardsPageUseCase(page = 1, pageSize = SEARCH_PAGE_SIZE, query = query)
            val latest = _state.value
            if (latest is HomeScreenState.Success) {
                _state.value =
                    latest.copy(
                        smallCards = applySorting(result.data),
                        allSmallCards = result.data,
                        isSearching = false,
                        listResetToken = latest.listResetToken + 1,
                    )
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Search failed: " + (e.message ?: "unknown"), e)
            val latest = _state.value
            if (latest is HomeScreenState.Success) {
                _state.value = latest.copy(isSearching = false)
            }
        }
    }

    private fun restoreBrowseCards() {
        val current = _state.value
        if (current is HomeScreenState.Success) {
            _state.value =
                current.copy(
                    smallCards = applySorting(browseCards),
                    allSmallCards = browseCards,
                    isSearching = false,
                    listResetToken = current.listResetToken + 1,
                )
        }
    }

    private fun applySorting(cards: List<SmallCardData>): List<SmallCardData> {
        return when (_sortType.value) {
            SortType.ALPHABETIC -> cards.sortedBy { it.title }
            SortType.MOST_POPULAR -> cards // Keep original order (assumed to be by popularity)
        }
    }

    private fun loadCards() =
        viewModelScope.launch {
            try {
                android.util.Log.d("HomeViewModel", "Loading cards...")
                val smallCards = getSmallCardsUseCase()
                val recentlyViewedCards = getRecentlyViewedCarsUseCase()
                android.util.Log.d(
                    "HomeViewModel",
                    "Loaded small=" + smallCards.size + " recent=" + recentlyViewedCards.size,
                )
                browseCards = smallCards
                _state.value =
                    HomeScreenState.Success(
                        smallCards = applySorting(smallCards),
                        allSmallCards = smallCards,
                        recentlyViewedCards = recentlyViewedCards,
                    )
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to load cards: " + (e.message ?: "unknown"), e)
                _state.value = HomeScreenState.Error(e.message ?: "Failed to load cards")
            }
        }

    fun refreshRecentlyViewed() =
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is HomeScreenState.Success) {
                try {
                    val recentlyViewedCards = getRecentlyViewedCarsUseCase()
                    Log.d("HomeViewModel", "Refreshed recently viewed: " + recentlyViewedCards.size)
                    _state.value = currentState.copy(recentlyViewedCards = recentlyViewedCards)
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Failed to refresh recently viewed: " + (e.message ?: "unknown"), e)
                }
            }
        }

    fun navigateToDetail(cardId: String) {
        viewModelScope.launch {
            try {
                saveRecentlyViewedCarUseCase(cardId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to save recently viewed car: ${e.message}")
            }
        }
        val (modelSlug, fuelAcronym, year) = parseVehicleSpec(cardId)
        navigate(CardDetailRoute(modelSlug, fuelAcronym, year), NavOptions(singleTop = true))
    }

    fun navigateToSelectComparison() {
        navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
    }

    fun navigateToFavorites() {
        navigate(FavoritesRoute, NavOptions(singleTop = true))
    }

    fun navigateToProfile() {
        navigate(ProfileRoute, NavOptions(singleTop = true))
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.ReloadCards -> {
                Log.d("HomeViewModel", "Reload event received")
                loadCards()
            }
        }
    }

    private companion object {
        const val MIN_SEARCH_LENGTH = 2
        const val SEARCH_DEBOUNCE_MS = 2000L
        const val SEARCH_PAGE_SIZE = 30
    }
}

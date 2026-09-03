package com.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.common.utils.stateInWhileSubscribed
import com.data.model.FavoriteCar
import com.data.model.SmallCardData
import com.data.usecase.GetRecentlyViewedCarsUseCase
import com.data.usecase.GetSmallCardsPageUseCase
import com.data.usecase.GetSmallCardsUseCase
import com.data.usecase.ObserveFavoriteIdsUseCase
import com.data.usecase.SaveRecentlyViewedCarUseCase
import com.data.usecase.ToggleFavoriteUseCase
import com.navigation.routes.CardDetailRoute
import com.navigation.routes.parseVehicleSpec
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getSmallCardsUseCase: GetSmallCardsUseCase,
    private val getSmallCardsPageUseCase: GetSmallCardsPageUseCase,
    private val getRecentlyViewedCarsUseCase: GetRecentlyViewedCarsUseCase,
    private val saveRecentlyViewedCarUseCase: SaveRecentlyViewedCarUseCase,
    observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    // Ids of currently favorited cars, so each card can render the correct heart state.
    val favoriteIds: StateFlow<Set<String>> =
        observeFavoriteIdsUseCase()
            .stateInWhileSubscribed(viewModelScope, emptySet(), STOP_TIMEOUT_MS)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchFocused = MutableStateFlow(false)
    val isSearchFocused: StateFlow<Boolean> = _isSearchFocused.asStateFlow()

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
                        smallCards = result.data,
                        allSmallCards = result.data,
                        isSearching = false,
                        hasSearchResults = true,
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
                    smallCards = browseCards,
                    allSmallCards = browseCards,
                    isSearching = false,
                    hasSearchResults = false,
                    listResetToken = current.listResetToken + 1,
                )
        }
    }

    private fun loadCards() =
        viewModelScope.launch {
            try {
                val smallCards = getSmallCardsUseCase()
                val recentlyViewedCards = getRecentlyViewedCarsUseCase()
                Log.d(
                    "HomeViewModel",
                    "Loaded small=" + smallCards.size + " recent=" + recentlyViewedCards.size,
                )
                browseCards = smallCards
                _state.value =
                    HomeScreenState.Success(
                        smallCards = smallCards,
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

    fun toggleFavorite(card: SmallCardData) =
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(card.toFavoriteCar())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to toggle favorite ${card.id}: ${e.message}", e)
            }
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
        const val STOP_TIMEOUT_MS = 5000L
    }
}

private fun SmallCardData.toFavoriteCar(): FavoriteCar =
    FavoriteCar(
        id = id,
        brand = title.substringBefore(" "),
        title = title,
        price = fipe,
        imageUrl = imageUrl,
    )

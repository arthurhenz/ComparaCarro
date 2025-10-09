package com.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.SmallCardData
import com.data.usecase.GetSmallCardsUseCase
import com.data.usecase.GetRecentlyViewedCarsUseCase
import com.data.usecase.SaveRecentlyViewedCarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

enum class SortType {
    MOST_POPULAR,
    ALPHABETIC
}

@KoinViewModel
class HomeViewModel (
    private val getSmallCardsUseCase: GetSmallCardsUseCase,
    private val getRecentlyViewedCarsUseCase: GetRecentlyViewedCarsUseCase,
    private val saveRecentlyViewedCarUseCase: SaveRecentlyViewedCarUseCase
): ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchFocused = MutableStateFlow(false)
    val isSearchFocused: StateFlow<Boolean> = _isSearchFocused.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.MOST_POPULAR)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()

    init {
        loadCards()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterCards()
    }

    fun updateSearchFocus(isFocused: Boolean) {
        _isSearchFocused.value = isFocused
    }

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
        filterCards()
    }

    private fun filterCards() {
        val currentState = _state.value
        if (currentState is HomeScreenState.Success) {
            val query = _searchQuery.value.trim()
            val filteredSmallCards = if (query.isEmpty()) {
                currentState.allSmallCards
            } else {
                currentState.allSmallCards.filter { card ->
                    card.title.contains(query, ignoreCase = true)
                }
            }
            val sortedCards = applySorting(filteredSmallCards)
            _state.value = currentState.copy(smallCards = sortedCards)
        }
    }

    private fun applySorting(cards: List<SmallCardData>): List<SmallCardData> {
        return when (_sortType.value) {
            SortType.ALPHABETIC -> cards.sortedBy { it.title }
            SortType.MOST_POPULAR -> cards // Keep original order (assumed to be by popularity)
        }
    }

    private fun loadCards() = viewModelScope.launch {
        try {
            android.util.Log.d("HomeViewModel", "Loading cards...")
            val smallCards = getSmallCardsUseCase()
            val recentlyViewedCards = getRecentlyViewedCarsUseCase()
            android.util.Log.d(
                "HomeViewModel",
                "Loaded small=" + smallCards.size + " recent=" + recentlyViewedCards.size
            )
            _state.value = HomeScreenState.Success(
                smallCards = smallCards,
                allSmallCards = smallCards,
                recentlyViewedCards = recentlyViewedCards
            )
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Failed to load cards: " + (e.message ?: "unknown"), e)
            _state.value = HomeScreenState.Error(e.message ?: "Failed to load cards")
        }
    }

    fun refreshRecentlyViewed() = viewModelScope.launch {
        val currentState = _state.value
        if (currentState is HomeScreenState.Success) {
            try {
                val recentlyViewedCards = getRecentlyViewedCarsUseCase()
                android.util.Log.d("HomeViewModel", "Refreshed recently viewed: " + recentlyViewedCards.size)
                _state.value = currentState.copy(recentlyViewedCards = recentlyViewedCards)
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Failed to refresh recently viewed: " + (e.message ?: "unknown"), e)
            }
        }
    }

    fun onCardClick(cardId: String) = viewModelScope.launch {
        try {
            saveRecentlyViewedCarUseCase(cardId)
            android.util.Log.d("HomeViewModel", "Saved recently viewed car: $cardId")
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Failed to save recently viewed car: ${e.message}")
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.ReloadCards -> {
                android.util.Log.d("HomeViewModel", "Reload event received")
                loadCards()
            }
        }
    }
}

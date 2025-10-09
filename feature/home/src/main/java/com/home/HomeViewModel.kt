package com.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.model.SmallCardData
import com.data.usecase.GetLargeCardsUseCase
import com.data.usecase.GetSmallCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel (
    private val getLargeCardsUseCase: GetLargeCardsUseCase,
    private val getSmallCardsUseCase: GetSmallCardsUseCase
): ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchFocused = MutableStateFlow(false)
    val isSearchFocused: StateFlow<Boolean> = _isSearchFocused.asStateFlow()

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
            _state.value = currentState.copy(smallCards = filteredSmallCards)
        }
    }

    private fun loadCards() = viewModelScope.launch {
        try {
            android.util.Log.d("HomeViewModel", "Loading cards...")
            val largeCards = getLargeCardsUseCase()
            val smallCards = getSmallCardsUseCase()
            android.util.Log.d(
                "HomeViewModel",
                "Loaded large=" + largeCards.size + " small=" + smallCards.size
            )
            _state.value = HomeScreenState.Success(
                largeCards = largeCards,
                smallCards = smallCards,
                allSmallCards = smallCards
            )
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Failed to load cards: " + (e.message ?: "unknown"), e)
            _state.value = HomeScreenState.Error(e.message ?: "Failed to load cards")
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

    // Public method to allow explicit refresh from UI or other layers
    fun refresh() {
        android.util.Log.d("HomeViewModel", "Manual refresh triggered")
        _state.value = HomeScreenState.Loading
        loadCards()
    }
}

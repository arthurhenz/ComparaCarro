package com.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        loadCards()
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
                smallCards = smallCards
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

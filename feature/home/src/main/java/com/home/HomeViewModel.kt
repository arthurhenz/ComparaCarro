package com.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetLargeCardsUseCase
import com.data.usecase.GetSmallCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
    private val getLargeCardsUseCase: GetLargeCardsUseCase by inject()
    private val getSmallCardsUseCase: GetSmallCardsUseCase by inject()

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        try {
            val largeCards = getLargeCardsUseCase()
            val smallCards = getSmallCardsUseCase()
            _state.value = HomeScreenState.Success(
                largeCards = largeCards,
                smallCards = smallCards
            )
        } catch (e: Exception) {
            _state.value = HomeScreenState.Error(e.message ?: "Failed to load cards")
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.ReloadCards -> {
                loadCards()
            }
        }
    }
}

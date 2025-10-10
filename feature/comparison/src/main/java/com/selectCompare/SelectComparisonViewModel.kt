package com.selectCompare


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetSmallCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class SelectComparisonViewModel(
    private val getSmallCardsUseCase: GetSmallCardsUseCase,
    @InjectedParam private val cardId: String
) : ViewModel() {

    private val _state = MutableStateFlow<SelectComparisonScreenState>(SelectComparisonScreenState.Loading)
    val state: StateFlow<SelectComparisonScreenState> = _state.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        try {
            val allSmallCards = getSmallCardsUseCase()
            val initialSelected = cardId.takeIf { it.isNotBlank() }
            val marked = allSmallCards.map { card ->
                if (card.id == initialSelected) card.copy(selected = true) else card
            }
            _state.value = SelectComparisonScreenState.Success(
                firstSelectedId = initialSelected,
                smallCards = marked,
                allSmallCards = marked,
                searchQuery = "",
                isSearchFocused = false
            )
        } catch (e: Exception) {
            _state.value = SelectComparisonScreenState.Error(e.message ?: "Failed to load card Comparisons")
        }
    }

    fun onEvent(event: SelectComparisonScreenEvent) {
        when (event) {
            SelectComparisonScreenEvent.ReloadCard -> {
                loadCards()
            }
            is SelectComparisonScreenEvent.ToggleFavorite -> {
                // Handle favorite toggle logic here
                // This would typically call a use case to update favorite status
            }
            is SelectComparisonScreenEvent.LoadRelatedCards -> {
                // Handle loading related cards
                loadCards()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            val filtered = if (query.isBlank()) current.allSmallCards else current.allSmallCards.filter {
                it.title.contains(query.trim(), ignoreCase = true)
            }
            _state.value = current.copy(
                smallCards = filtered,
                searchQuery = query
            )
        }
    }

    fun updateSearchFocus(isFocused: Boolean) {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            _state.value = current.copy(isSearchFocused = isFocused)
        }
    }

    fun toggleSelection(cardId: String) {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            val currentSelectedIds = current.allSmallCards.filter { it.selected }.map { it.id }.toMutableList()
            val isSelected = currentSelectedIds.contains(cardId)
            if (isSelected) {
                currentSelectedIds.remove(cardId)
            } else {
                if (currentSelectedIds.size >= 2) return
                currentSelectedIds.add(cardId)
            }

            val updatedAll = current.allSmallCards.map { it.copy(selected = currentSelectedIds.contains(it.id)) }
            val updatedFiltered = current.smallCards.map { it.copy(selected = currentSelectedIds.contains(it.id)) }

            _state.value = current.copy(
                allSmallCards = updatedAll,
                smallCards = updatedFiltered
            )
        }
    }
}

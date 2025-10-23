package com.selectCompare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.usecase.GetSmallCardsPageUseCase
import com.data.usecase.GetSmallCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class SelectComparisonViewModel(
    private val getSmallCardsPageUseCase: GetSmallCardsPageUseCase,
    @InjectedParam private val cardId: String
) : ViewModel() {
    private val _state = MutableStateFlow<SelectComparisonScreenState>(SelectComparisonScreenState.Loading)
    val state: StateFlow<SelectComparisonScreenState> = _state.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() =
        viewModelScope.launch {
            try {
                val firstPageSize = 30
                val firstPage = getSmallCardsPageUseCase(page = 1, pageSize = firstPageSize)
                val allSmallCards = firstPage.data
                val initialSelected = cardId.takeIf { it.isNotBlank() }
                val marked =
                    allSmallCards.map { card ->
                        if (card.id == initialSelected) card.copy(selected = true) else card
                    }
                _state.value =
                    SelectComparisonScreenState.Success(
                        firstSelectedId = initialSelected,
                        smallCards = marked,
                        allSmallCards = marked,
                        searchQuery = "",
                        isSearchFocused = false,
                        isLoadingMore = false,
                        nextPage = if (firstPage.hasNext) 2 else null,
                        pageSize = firstPage.pageSize,
                        hasNext = firstPage.hasNext
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
            }
            is SelectComparisonScreenEvent.LoadRelatedCards -> {
                loadCards()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            val filtered =
                if (query.isBlank()) {
                    current.allSmallCards
                } else {
                    current.allSmallCards.filter {
                        it.title.contains(query.trim(), ignoreCase = true)
                    }
                }
            _state.value =
                current.copy(
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

            _state.value =
                current.copy(
                    allSmallCards = updatedAll,
                    smallCards = updatedFiltered
                )
        }
    }

    fun loadNextPageIfNeeded(lastVisibleIndex: Int) {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            if (current.isLoadingMore || current.nextPage == null) return
            if (lastVisibleIndex >= current.smallCards.size - 4) {
                _state.value = current.copy(isLoadingMore = true)
                viewModelScope.launch {
                    try {
                        val nextPageNumber = current.nextPage
                        if (nextPageNumber != null) {
                            val page = getSmallCardsPageUseCase(page = nextPageNumber, pageSize = current.pageSize)
                            val appendedAll = current.allSmallCards + page.data
                            val appendedFiltered =
                                if (current.searchQuery.isBlank()) {
                                    appendedAll
                                } else {
                                    appendedAll.filter { it.title.contains(current.searchQuery.trim(), ignoreCase = true) }
                                }
                            _state.value =
                                current.copy(
                                    allSmallCards = appendedAll,
                                    smallCards = appendedFiltered,
                                    isLoadingMore = false,
                                    nextPage = if (page.hasNext) nextPageNumber + 1 else null,
                                    hasNext = page.hasNext
                                )
                        }
                    } catch (e: Exception) {
                        _state.value = current.copy(isLoadingMore = false)
                    }
                }
            }
        }
    }
}

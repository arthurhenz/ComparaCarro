package com.selectCompare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.SmallCardData
import com.data.usecase.GetSmallCardsPageUseCase
import com.navigation.routes.FavoritesRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SelectComparisonRoute
import com.ui.BottomNavTab
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class SelectComparisonViewModel(
    private val getSmallCardsPageUseCase: GetSmallCardsPageUseCase,
    @InjectedParam private val cardId: String,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow<SelectComparisonScreenState>(SelectComparisonScreenState.Loading)
    val state: StateFlow<SelectComparisonScreenState> = _state.asStateFlow()

    // Debounce handle so each keystroke cancels the previous pending FIPE request.
    private var searchJob: Job? = null

    init {
        loadCards()
    }

    private fun loadCards() =
        viewModelScope.launch {
            try {
                val firstPageSize = 30
                val firstPage = getSmallCardsPageUseCase(page = 1, pageSize = firstPageSize)
                val initialSelected = cardId.takeIf { it.isNotBlank() }
                val selectedCards =
                    firstPage.data
                        .filter { it.id == initialSelected }
                        .map { it.copy(selected = true) }
                val selectedIds = selectedCards.map { it.id }.toSet()
                val browseCards = firstPage.data.markSelected(selectedIds)
                _state.value =
                    SelectComparisonScreenState.Success(
                        firstSelectedId = initialSelected,
                        smallCards = browseCards,
                        browseCards = browseCards,
                        selectedCards = selectedCards,
                        searchQuery = "",
                        isSearchFocused = false,
                        isSearching = false,
                        isLoadingMore = false,
                        nextPage = if (firstPage.hasNext) 2 else null,
                        pageSize = firstPage.pageSize,
                    )
            } catch (e: Exception) {
                _state.value = SelectComparisonScreenState.Error(e.message ?: "Failed to load card Comparisons")
            }
        }

    fun navigateToBottomTab(tab: BottomNavTab) {
        when (tab) {
            BottomNavTab.Garagem -> navigate(HomeScreenRoute, NavOptions(popUpTo = HomeScreenRoute))
            BottomNavTab.Comparar -> navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
            BottomNavTab.Favoritos -> navigate(FavoritesRoute, NavOptions(singleTop = true))
            BottomNavTab.Perfil -> navigate(ProfileRoute, NavOptions(singleTop = true))
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
        if (current !is SelectComparisonScreenState.Success) return
        _state.value = current.copy(searchQuery = query)
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.length < MIN_SEARCH_LENGTH) {
            // Below the threshold (including empty) restores the cached browse list immediately.
            restoreBrowseCards()
            return
        }
        searchJob =
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_MS)
                performSearch(trimmed)
            }
    }

    private suspend fun performSearch(query: String) {
        val current = _state.value
        if (current !is SelectComparisonScreenState.Success) return
        _state.value = current.copy(isSearching = true)
        try {
            val result = getSmallCardsPageUseCase(page = 1, pageSize = SEARCH_PAGE_SIZE, query = query)
            val latest = _state.value
            if (latest is SelectComparisonScreenState.Success) {
                val selectedIds = latest.selectedCards.map { it.id }.toSet()
                _state.value =
                    latest.copy(
                        smallCards = result.data.markSelected(selectedIds),
                        isSearching = false,
                        // Search results are not paginated; pagination resumes on the browse list.
                        isLoadingMore = false,
                        listResetToken = latest.listResetToken + 1,
                    )
            }
        } catch (e: Exception) {
            val latest = _state.value
            if (latest is SelectComparisonScreenState.Success) {
                _state.value = latest.copy(isSearching = false)
            }
        }
    }

    private fun restoreBrowseCards() {
        val current = _state.value
        if (current is SelectComparisonScreenState.Success) {
            val selectedIds = current.selectedCards.map { it.id }.toSet()
            _state.value =
                current.copy(
                    smallCards = current.browseCards.markSelected(selectedIds),
                    isSearching = false,
                    listResetToken = current.listResetToken + 1,
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
        if (current !is SelectComparisonScreenState.Success) return

        val alreadySelected = current.selectedCards.any { it.id == cardId }
        val newSelected =
            if (alreadySelected) {
                current.selectedCards.filterNot { it.id == cardId }
            } else {
                if (current.selectedCards.size >= 2) return
                val card =
                    current.smallCards.firstOrNull { it.id == cardId }
                        ?: current.browseCards.firstOrNull { it.id == cardId }
                        ?: return
                current.selectedCards + card.copy(selected = true)
            }

        val selectedIds = newSelected.map { it.id }.toSet()
        _state.value =
            current.copy(
                selectedCards = newSelected.map { it.copy(selected = true) },
                smallCards = current.smallCards.markSelected(selectedIds),
                browseCards = current.browseCards.markSelected(selectedIds),
            )
    }

    fun loadNextPageIfNeeded(lastVisibleIndex: Int) {
        val current = _state.value
        if (current !is SelectComparisonScreenState.Success) return
        // Pagination only applies to the browse list; search results are a single page.
        if (current.searchQuery.trim().length >= MIN_SEARCH_LENGTH) return
        if (current.isLoadingMore || current.nextPage == null) return
        if (lastVisibleIndex < current.smallCards.size - 4) return

        _state.value = current.copy(isLoadingMore = true)
        viewModelScope.launch {
            try {
                val nextPageNumber = current.nextPage
                if (nextPageNumber != null) {
                    val page = getSmallCardsPageUseCase(page = nextPageNumber, pageSize = current.pageSize)
                    val latest = _state.value
                    if (latest is SelectComparisonScreenState.Success) {
                        val selectedIds = latest.selectedCards.map { it.id }.toSet()
                        val appendedBrowse = (latest.browseCards + page.data).markSelected(selectedIds)
                        // Only reflect the new page in the displayed list if the user isn't searching.
                        val isSearchActive = latest.searchQuery.trim().length >= MIN_SEARCH_LENGTH
                        _state.value =
                            latest.copy(
                                browseCards = appendedBrowse,
                                smallCards = if (isSearchActive) latest.smallCards else appendedBrowse,
                                isLoadingMore = false,
                                nextPage = if (page.hasNext) nextPageNumber + 1 else null,
                            )
                    }
                }
            } catch (e: Exception) {
                val latest = _state.value
                if (latest is SelectComparisonScreenState.Success) {
                    _state.value = latest.copy(isLoadingMore = false)
                }
            }
        }
    }

    private fun List<SmallCardData>.markSelected(selectedIds: Set<String>): List<SmallCardData> =
        map { it.copy(selected = selectedIds.contains(it.id)) }

    private companion object {
        const val MIN_SEARCH_LENGTH = 2
        const val SEARCH_DEBOUNCE_MS = 2000L
        const val SEARCH_PAGE_SIZE = 50
    }
}

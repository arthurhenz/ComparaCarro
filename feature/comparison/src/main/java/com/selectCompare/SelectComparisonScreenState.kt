package com.selectCompare

import com.data.model.SmallCardData

sealed class SelectComparisonScreenState {
    data object Loading : SelectComparisonScreenState()
    data class Error(val error: String?) : SelectComparisonScreenState()
    data class Success(
        val firstSelectedId: String?,
        // Currently displayed list: browse pages while idle, FIPE search results while searching.
        val smallCards: List<SmallCardData>,
        // Accumulated browse pages, kept aside so clearing the search restores them instantly.
        val browseCards: List<SmallCardData>,
        // Selection source of truth (max 2), independent of which list is currently displayed.
        val selectedCards: List<SmallCardData>,
        val searchQuery: String,
        val isSearchFocused: Boolean,
        val isSearching: Boolean = false,
        val isLoadingMore: Boolean = false,
        // Browse pagination cursor; paused (ignored) while a search is active.
        val nextPage: Int? = null,
        val pageSize: Int = 30,
        // Bumped whenever the displayed list is replaced wholesale (search results / browse restore),
        // so the UI can scroll back to the top. Not bumped on pagination appends.
        val listResetToken: Int = 0,
    ) : SelectComparisonScreenState()
}

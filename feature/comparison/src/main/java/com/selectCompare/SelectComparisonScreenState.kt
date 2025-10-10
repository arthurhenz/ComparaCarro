package com.selectCompare

import com.data.model.SmallCardData

sealed class SelectComparisonScreenState {
    data object Loading : SelectComparisonScreenState()
    data class Error(val error: String?) : SelectComparisonScreenState()
    data class Success(
        val firstSelectedId: String?,
        val smallCards: List<SmallCardData>,
        val allSmallCards: List<SmallCardData>,
        val searchQuery: String,
        val isSearchFocused: Boolean
    ) : SelectComparisonScreenState()
}

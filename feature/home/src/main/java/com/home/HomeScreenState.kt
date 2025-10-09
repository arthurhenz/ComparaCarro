package com.home

import com.data.model.LargeCardData
import com.data.model.SmallCardData

sealed class HomeScreenState {
    data object Loading : HomeScreenState()
    data class Error(val error: String?) : HomeScreenState()
    data class Success(
        val smallCards: List<SmallCardData>,
        val allSmallCards: List<SmallCardData>,
        val recentlyViewedCards: List<LargeCardData> = emptyList()
    ) : HomeScreenState()
}

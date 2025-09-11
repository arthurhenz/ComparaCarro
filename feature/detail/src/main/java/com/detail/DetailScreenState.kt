package com.detail

import com.data.model.LargeCardData
import com.data.model.SmallCardData

sealed class DetailScreenState {
    data object Loading : DetailScreenState()
    data class Error(val error: String?) : DetailScreenState()
    data class Success(
        val cardId: String,
        val cardTitle: String,
        val cardDescription: String = "",
        val relatedCards: List<SmallCardData> = emptyList()
    ) : DetailScreenState()
}

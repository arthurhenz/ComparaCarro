package com.detail

import com.data.model.CarDetailData

sealed class DetailScreenState {
    data object Loading : DetailScreenState()
    data class Error(val error: String?) : DetailScreenState()
    data class Success(
        val car: CarDetailData
    ) : DetailScreenState()
}

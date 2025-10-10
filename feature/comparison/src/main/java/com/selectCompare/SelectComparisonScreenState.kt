package com.selectCompare

import com.data.model.CarDetailData

sealed class SelectComparisonScreenState {
    data object Loading : SelectComparisonScreenState()
    data class Error(val error: String?) : SelectComparisonScreenState()
    data class Success(
        val car: CarDetailData
    ) : SelectComparisonScreenState()
}

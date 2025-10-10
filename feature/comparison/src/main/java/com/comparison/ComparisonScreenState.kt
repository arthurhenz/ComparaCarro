package com.comparison

import com.data.model.CarDetailData

sealed class ComparisonScreenState {
    data object Loading : ComparisonScreenState()
    data class Error(val error: String?) : ComparisonScreenState()
    data class Success(
        val firstCar: CarDetailData,
        val secondCar: CarDetailData
    ) : ComparisonScreenState()
}

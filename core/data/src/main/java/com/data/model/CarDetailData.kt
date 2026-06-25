package com.data.model

data class CarDetailData(
    val id: String,
    val title: String,
    val price: String,
    val category: String,
    val views: Int,
    val optionals: List<String>,
    val year: Int = 0,
    val fipeCode: String = "",
    // ---- Real fipeX data (from /v1/prices/expanded) ----
    val makeName: String = "",
    val modelName: String = "",
    val fuelName: String = "",
    val fuelAcronym: String = "",
    val vehicleType: String = "",
    val referenceLabel: String = "",
    val analytics: CarAnalytics? = null,
    val availableYears: List<Int> = emptyList(),
    val priceHistory: List<CarPricePoint> = emptyList(),
)

/** Price analytics returned by fipeX. Percentages are raw ratios (e.g. -0.30 = -30%). */
data class CarAnalytics(
    val changeFromPreviousMonthPct: Double? = null,
    val changeFromLaunchPct: Double? = null,
    val peakToNowPctChange: Double? = null,
    val priceVolatility: Double? = null,
    val priceRank: Int? = null,
    val priceRankTotalInCategory: Int? = null,
    val valueRetentionPct: Double? = null,
    val annualDepreciationRate: Double? = null,
    val lifecycleStatus: String? = null,
    val anomalyStatus: String? = null,
    val anomalyZScore: Double? = null,
)

data class CarPricePoint(
    val year: Int,
    val month: Int,
    val price: String,
)

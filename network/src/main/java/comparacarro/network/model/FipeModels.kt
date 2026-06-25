package comparacarro.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Backend models for the fipeX API (https://api.fipex.com.br/v1).
 *
 * Listing endpoints wrap results in a `data` array. `/v1/search` instead returns
 * `data` + `estimated_total_hits` (no pagination block). Prices are always integer
 * cents; a pre-formatted BRL string accompanies them.
 */

// ---- Vehicle types (/v1/vehicle-types) ----

@Serializable
data class VehicleTypesResponse(
    @SerialName("data") val data: List<VehicleType> = emptyList(),
)

@Serializable
data class VehicleType(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String? = null,
)

// ---- Search (/v1/search) ----

@Serializable
data class SearchResponse(
    @SerialName("data") val data: List<SearchItem> = emptyList(),
    @SerialName("estimated_total_hits") val estimatedTotalHits: Int = 0,
    @SerialName("time_taken_ms") val timeTakenMs: Int = 0,
)

@Serializable
data class SearchItem(
    @SerialName("price_id") val priceId: String = "",
    @SerialName("model_id") val modelId: String = "",
    @SerialName("model_name") val modelName: String = "",
    @SerialName("model_slug") val modelSlug: String = "",
    @SerialName("make_id") val makeId: String = "",
    @SerialName("make_name") val makeName: String = "",
    @SerialName("make_slug") val makeSlug: String = "",
    @SerialName("fuel_id") val fuelId: String = "",
    @SerialName("fuel_name") val fuelName: String = "",
    // The search endpoint misspells this field as "fuel_acroym" (compare/expanded use "acronym").
    @SerialName("fuel_acroym") val fuelAcronym: String = "",
    @SerialName("model_year") val modelYear: Int = 0,
    @SerialName("latest_market_price_cents") val latestMarketPriceCents: Long = 0,
    @SerialName("type_id") val typeId: String = "",
    @SerialName("type_name") val typeName: String = "",
)

// ---- Expanded price (/v1/prices/expanded) ----

@Serializable
data class ExpandedPriceResponse(
    @SerialName("data") val data: ExpandedPrice,
)

@Serializable
data class ExpandedPrice(
    @SerialName("price") val price: PriceData,
    @SerialName("analytics") val analytics: PriceAnalytics? = null,
    @SerialName("history") val history: List<PricePoint> = emptyList(),
    @SerialName("available_years") val availableYears: List<AvailableYear> = emptyList(),
)

@Serializable
data class PriceData(
    @SerialName("price") val price: Long = 0,
    @SerialName("formatted_price") val formattedPrice: String = "",
    @SerialName("model_year") val modelYear: Int = 0,
    @SerialName("make") val make: MakeRef? = null,
    @SerialName("model") val model: ModelRef? = null,
    @SerialName("fuel") val fuel: FuelRef? = null,
    @SerialName("type") val type: TypeRef? = null,
    @SerialName("reference") val reference: ReferenceRef? = null,
)

@Serializable
data class MakeRef(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("slug") val slug: String = "",
)

@Serializable
data class ModelRef(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("make_id") val makeId: String = "",
    @SerialName("slug") val slug: String = "",
)

@Serializable
data class FuelRef(
    @SerialName("id") val id: String = "",
    @SerialName("acronym") val acronym: String = "",
    @SerialName("name") val name: String = "",
)

@Serializable
data class TypeRef(
    @SerialName("id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("slug") val slug: String = "",
)

@Serializable
data class ReferenceRef(
    @SerialName("id") val id: String = "",
    @SerialName("month") val month: Int = 0,
    @SerialName("month_name") val monthName: String = "",
    @SerialName("year") val year: Int = 0,
)

@Serializable
data class PriceAnalytics(
    @SerialName("change_from_previous_month_pct") val changeFromPreviousMonthPct: Double? = null,
    @SerialName("change_from_launch_pct") val changeFromLaunchPct: Double? = null,
    @SerialName("peak_to_now_pct_change") val peakToNowPctChange: Double? = null,
    @SerialName("price_volatility") val priceVolatility: Double? = null,
    @SerialName("price_rank") val priceRank: Int? = null,
    @SerialName("price_rank_total_in_category") val priceRankTotalInCategory: Int? = null,
    @SerialName("value_retention_pct") val valueRetentionPct: Double? = null,
    @SerialName("annual_depreciation_rate") val annualDepreciationRate: Double? = null,
    @SerialName("lifecycle_status") val lifecycleStatus: String? = null,
    @SerialName("anomaly_status") val anomalyStatus: String? = null,
    @SerialName("anomaly_z_score") val anomalyZScore: Double? = null,
)

@Serializable
data class PricePoint(
    @SerialName("year") val year: Int = 0,
    @SerialName("month") val month: Int = 0,
    @SerialName("market_price_cents") val marketPriceCents: Long = 0,
    @SerialName("formatted_price") val formattedPrice: String = "",
)

@Serializable
data class AvailableYear(
    @SerialName("model_year") val modelYear: Int = 0,
)

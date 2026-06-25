package com.data.repository

import com.data.model.CarAnalytics
import com.data.model.CarDetailData
import com.data.model.CarPricePoint
import com.data.model.LargeCardData
import com.data.model.PaginationResult
import com.data.model.SmallCardData
import comparacarro.network.api.CarImagesApi
import comparacarro.network.api.CarsApi
import comparacarro.network.model.CarImageRequest
import comparacarro.network.model.PriceAnalytics
import comparacarro.network.model.SearchItem
import comparacarro.network.result.NetworkResult
import org.koin.core.annotation.Single

@Single
class CardRepositoryImpl(
    private val carsApi: CarsApi,
    private val carImagesApi: CarImagesApi,
) : CardRepository {
    // The "carro" vehicle-type UUID is resolved once from /v1/vehicle-types and reused
    // to constrain every search to cars only. Resolving it twice on a startup race is harmless.
    @Volatile
    private var carTypeId: String? = null

    override suspend fun getLargeCards(): List<LargeCardData> {
        return when (val result = searchCars(query = null, page = 1, limit = 15)) {
            is NetworkResult.Success -> {
                val items = result.data.data.take(2)
                val imageUrls = resolveImages(items, LIST_IMAGE_WIDTH)
                items.mapIndexed { index, item ->
                    LargeCardData(
                        id = item.toSpec(),
                        title = item.toTitle(),
                        imageUrl = imageUrls.getOrNull(index),
                    )
                }
            }
            is NetworkResult.Error -> {
                logError("getLargeCards", result)
                emptyList()
            }
        }
    }

    override suspend fun getCar(
        modelSlug: String,
        fuelAcronym: String,
        year: String,
    ): CarDetailData {
        return when (val result = carsApi.getExpandedPrice(modelSlug, fuelAcronym, year)) {
            is NetworkResult.Success -> {
                val expanded = result.data.data
                val price = expanded.price
                val imageUrl =
                    carImagesApi.getSignedUrls(
                        listOf(
                            CarImageRequest(
                                make = price.make?.name.orEmpty(),
                                model = price.model?.name,
                                year = price.modelYear.takeIf { it > 0 },
                                width = DETAIL_IMAGE_WIDTH,
                            ),
                        ),
                    ).firstOrNull()
                CarDetailData(
                    id = spec(price.model?.slug ?: modelSlug, price.fuel?.acronym ?: fuelAcronym, price.modelYear),
                    title = listOfNotNull(price.make?.name, price.model?.name).joinToString(" ").trim(),
                    price = price.formattedPrice,
                    category = price.make?.name.orEmpty(),
                    views = 0,
                    optionals = emptyList(),
                    year = price.modelYear,
                    fipeCode = "",
                    imageUrl = imageUrl,
                    makeName = price.make?.name.orEmpty(),
                    modelName = price.model?.name.orEmpty(),
                    fuelName = price.fuel?.name.orEmpty(),
                    fuelAcronym = price.fuel?.acronym.orEmpty(),
                    vehicleType = price.type?.name.orEmpty(),
                    referenceLabel = price.reference?.let { "${it.monthName}/${it.year}" }.orEmpty(),
                    analytics = expanded.analytics?.toDomain(),
                    availableYears = expanded.availableYears.map { it.modelYear },
                    priceHistory = expanded.history.map { CarPricePoint(it.year, it.month, it.formattedPrice) },
                )
            }
            is NetworkResult.Error -> {
                logError("getCar", result)
                throw IllegalStateException(result.message ?: "Unknown error")
            }
        }
    }

    override suspend fun getSmallCards(): List<SmallCardData> = getSmallCardsPage(page = 1, pageSize = 30).data

    override suspend fun getSmallCardsPage(
        page: Int,
        pageSize: Int,
    ): PaginationResult<SmallCardData> {
        // fipeX caps the page size at 50.
        val limit = pageSize.coerceIn(1, MAX_PAGE_SIZE)
        return when (val result = searchCars(query = null, page = page, limit = limit)) {
            is NetworkResult.Success -> {
                val response = result.data
                val total = response.estimatedTotalHits
                val totalPages = if (total <= 0) page else (total + limit - 1) / limit
                val imageUrls = resolveImages(response.data, LIST_IMAGE_WIDTH)
                PaginationResult(
                    data = response.data.mapIndexed { index, item -> item.toSmallCard(imageUrls.getOrNull(index)) },
                    page = page,
                    pageSize = limit,
                    totalItems = total,
                    totalPages = totalPages,
                    hasNext = response.data.size == limit && page < totalPages,
                    hasPrevious = page > 1,
                )
            }
            is NetworkResult.Error -> {
                logError("getSmallCardsPage", result)
                PaginationResult(
                    data = emptyList(),
                    page = page,
                    pageSize = limit,
                    totalItems = 0,
                    totalPages = 0,
                    hasNext = false,
                    hasPrevious = page > 1,
                )
            }
        }
    }

    private suspend fun searchCars(
        query: String?,
        page: Int,
        limit: Int,
    ) = carsApi.searchCars(query = query, page = page, limit = limit, typeId = carTypeId())

    /** Resolves and caches the "carro" vehicle-type UUID. Returns null if it can't be determined. */
    private suspend fun carTypeId(): String? {
        carTypeId?.let { return it }
        return when (val result = carsApi.getVehicleTypes()) {
            is NetworkResult.Success -> {
                // The "carro" slug ("C") collides with "caminhão", so match by name instead.
                val id = result.data.data.firstOrNull { it.name.equals("carro", ignoreCase = true) }?.id
                carTypeId = id
                id
            }
            is NetworkResult.Error -> {
                logError("getVehicleTypes", result)
                null
            }
        }
    }

    /** Batch-resolves signed image URLs for a list of search results (positionally aligned). */
    private suspend fun resolveImages(items: List<SearchItem>, width: Int): List<String?> =
        carImagesApi.getSignedUrls(
            items.map { item ->
                CarImageRequest(
                    make = item.makeName,
                    model = item.modelName,
                    year = item.modelYear.takeIf { it > 0 },
                    width = width,
                )
            },
        )

    private fun SearchItem.toSmallCard(imageUrl: String?) =
        SmallCardData(
            id = toSpec(),
            title = toTitle(),
            fipe = formatCents(latestMarketPriceCents),
            selected = false,
            imageUrl = imageUrl,
        )

    private fun SearchItem.toSpec() = spec(modelSlug, fuelAcronym, modelYear)

    private fun SearchItem.toTitle() = listOf(makeName, modelName, modelYear.toString()).joinToString(" ").trim()

    private fun PriceAnalytics.toDomain() =
        CarAnalytics(
            changeFromPreviousMonthPct = changeFromPreviousMonthPct,
            changeFromLaunchPct = changeFromLaunchPct,
            peakToNowPctChange = peakToNowPctChange,
            priceVolatility = priceVolatility,
            priceRank = priceRank,
            priceRankTotalInCategory = priceRankTotalInCategory,
            valueRetentionPct = valueRetentionPct,
            annualDepreciationRate = annualDepreciationRate,
            lifecycleStatus = lifecycleStatus,
            anomalyStatus = anomalyStatus,
            anomalyZScore = anomalyZScore,
        )

    private fun logError(operation: String, error: NetworkResult.Error) {
        android.util.Log.e(
            "CardRepositoryImpl",
            "$operation error code=" + (error.code ?: -1) + " message=" + (error.message ?: "unknown"),
        )
    }

    private companion object {
        const val MAX_PAGE_SIZE = 50
        const val LIST_IMAGE_WIDTH = 400
        const val DETAIL_IMAGE_WIDTH = 800

        fun spec(modelSlug: String, fuelAcronym: String, year: Int) = "$modelSlug,$fuelAcronym,$year"

        /** Formats integer cents to a BRL string (e.g. 710300 -> "R$ 7.103,00") without locale dependencies. */
        fun formatCents(cents: Long): String {
            val reais = cents / 100
            val centavos = (cents % 100).toInt()
            val grouped = reais.toString().reversed().chunked(3).joinToString(".").reversed()
            return "R$ %s,%02d".format(grouped, centavos)
        }
    }
}

package com.data.repository

import com.data.model.CarDetailData
import com.data.model.LargeCardData
import com.data.model.PaginationResult
import com.data.model.SmallCardData
import comparacarro.network.api.CarsApi
import comparacarro.network.result.NetworkResult
import org.koin.core.annotation.Single

@Single
class CardRepositoryImpl(
    private val carsApi: CarsApi
) : CardRepository {
    override suspend fun getLargeCards(): List<LargeCardData> {
        return when (val result = carsApi.getCars(page = 1, pageSize = 15)) {
            is NetworkResult.Success ->
                result.data.data.take(2).map { car ->
                    LargeCardData(
                        id = car.id.toString(),
                        title = "${car.marca} ${car.modelo}"
                    )
                }
            is NetworkResult.Error -> {
                android.util.Log.e(
                    "CardRepositoryImpl",
                    "getLargeCards error code=" + (result.code ?: -1) + " message=" + (result.message ?: "unknown")
                )
                emptyList()
            }
        }
    }

    override suspend fun getCarById(id: Int): CarDetailData {
        return when (val result = carsApi.getCarById(id)) {
            is NetworkResult.Success -> {
                val car = result.data
                CarDetailData(
                    id = car.id.toString(),
                    title = "${car.marca} ${car.modelo}",
                    price = formatPrice(car.valor.toFloat()),
                    category = car.marca,
                    views = 0,
                    optionals = emptyList()
                )
            }
            is NetworkResult.Error -> {
                android.util.Log.e(
                    "CardRepositoryImpl",
                    "getCarById error code=" + (result.code ?: -1) + " message=" + (result.message ?: "unknown")
                )
                throw IllegalStateException(result.message ?: "Unknown error")
            }
        }
    }

    override suspend fun getSmallCards(): List<SmallCardData> {
        val firstPage = getSmallCardsPage(page = 1, pageSize = 30)
        return firstPage.data
    }

    override suspend fun getSmallCardsPage(
        page: Int,
        pageSize: Int
    ): PaginationResult<SmallCardData> {
        return when (val result = carsApi.getCars(page = page, pageSize = pageSize)) {
            is NetworkResult.Success -> {
                val pageData = result.data
                PaginationResult(
                    data =
                        pageData.data.map { car ->
                            SmallCardData(
                                id = car.id.toString(),
                                title = "${car.marca} ${car.modelo}",
                                fipe = formatPrice(car.valor.toFloat()),
                                selected = false
                            )
                        },
                    page = pageData.page,
                    pageSize = pageData.pageSize,
                    totalItems = pageData.totalItems,
                    totalPages = pageData.totalPages,
                    hasNext = pageData.hasNext,
                    hasPrevious = pageData.hasPrevious
                )
            }
            is NetworkResult.Error -> {
                android.util.Log.e(
                    "CardRepositoryImpl",
                    "getSmallCardsPage error code=" + (result.code ?: -1) + " message=" + (result.message ?: "unknown")
                )
                PaginationResult(
                    data = emptyList(),
                    page = page,
                    pageSize = pageSize,
                    totalItems = 0,
                    totalPages = 0,
                    hasNext = false,
                    hasPrevious = page > 1
                )
            }
        }
    }

    private fun formatPrice(fipe: Float): String {
        // Simple BRL formatting placeholder without locale deps
        val value = String.format("%.2f", fipe)
        return "R$ $value"
    }
}

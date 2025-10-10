package com.data.repository

import com.data.model.CarDetailData
import com.data.model.LargeCardData
import com.data.model.SmallCardData
import comparacarro.network.api.CarsApi
import comparacarro.network.result.NetworkResult
import org.koin.core.annotation.Single

@Single
class CardRepositoryImpl(
    private val carsApi: CarsApi
) : CardRepository {
    override suspend fun getLargeCards(): List<LargeCardData> {
        return when (val result = carsApi.getCars()) {
            is NetworkResult.Success ->
                result.data.take(2).map { car ->
                    LargeCardData(
                        id = car.id.toString(),
                        title = car.title
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
                    title = car.title,
                    price = formatPrice(car.fipe),
                    category = car.category.name,
                    views = car.views,
                    optionals = car.opcionais.map { it.name }
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
        return when (val result = carsApi.getCars()) {
            is NetworkResult.Success -> {
                android.util.Log.d(
                    "CardRepositoryImpl",
                    "getSmallCards cars=" + result.data.size
                )
                result.data.map { car ->
                    SmallCardData(
                        id = car.id.toString(),
                        title = car.title,
                        fipe = formatPrice(car.fipe),
                        selected = false
                    )
                }
            }
            is NetworkResult.Error -> {
                android.util.Log.e(
                    "CardRepositoryImpl",
                    "getSmallCards error code=" + (result.code ?: -1) + " message=" + (result.message ?: "unknown")
                )
                emptyList()
            }
        }
    }

    private fun formatPrice(fipe: Float): String {
        // Simple BRL formatting placeholder without locale deps
        val value = String.format("%.2f", fipe)
        return "R$ $value"
    }
}

package comparacarro2.network.api

import comparacarro2.network.model.BackendCar
import comparacarro2.network.model.BackendCarDetail
import comparacarro2.network.model.PaginatedResponse
import comparacarro2.network.result.NetworkResult

interface CarsApi {
    suspend fun getCars(page: Int, pageSize: Int): NetworkResult<PaginatedResponse<BackendCar>>
    suspend fun getCarById(id: Int): NetworkResult<BackendCarDetail>
}


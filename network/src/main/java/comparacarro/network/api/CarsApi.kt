package comparacarro.network.api

import comparacarro.network.model.BackendCar
import comparacarro.network.model.BackendCarDetail
import comparacarro.network.model.PaginatedResponse
import comparacarro.network.result.NetworkResult

interface CarsApi {
    suspend fun getCars(page: Int, pageSize: Int): NetworkResult<PaginatedResponse<BackendCar>>
    suspend fun getCarById(id: Int): NetworkResult<BackendCarDetail>
}


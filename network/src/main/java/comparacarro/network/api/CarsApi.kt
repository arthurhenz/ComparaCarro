package comparacarro.network.api

import comparacarro.network.model.BackendCar
import comparacarro.network.model.BackendCarDetail
import comparacarro.network.result.NetworkResult

interface CarsApi {
    suspend fun getCars(): NetworkResult<List<BackendCar>>
    suspend fun getCarById(id: Int): NetworkResult<BackendCarDetail>
}


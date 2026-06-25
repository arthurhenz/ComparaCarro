package comparacarro.network.api

import comparacarro.network.model.ExpandedPriceResponse
import comparacarro.network.model.SearchResponse
import comparacarro.network.model.VehicleTypesResponse
import comparacarro.network.result.NetworkResult

interface CarsApi {
    /** Lists the available vehicle types (carro, moto, caminhão). */
    suspend fun getVehicleTypes(): NetworkResult<VehicleTypesResponse>

    /**
     * Full-text vehicle search. Optionally constrained to a single vehicle type
     * via the advanced `filters[]` notation.
     */
    suspend fun searchCars(
        query: String?,
        page: Int,
        limit: Int,
        typeId: String?,
    ): NetworkResult<SearchResponse>

    /** Expanded price for one vehicle: current price, analytics, history and available years. */
    suspend fun getExpandedPrice(
        modelSlug: String,
        fuelAcronym: String,
        year: String,
    ): NetworkResult<ExpandedPriceResponse>
}

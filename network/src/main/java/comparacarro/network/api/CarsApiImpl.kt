package comparacarro.network.api

import comparacarro.network.model.ExpandedPriceResponse
import comparacarro.network.model.SearchResponse
import comparacarro.network.model.VehicleTypesResponse
import comparacarro.network.result.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class CarsApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : CarsApi {
    override suspend fun getVehicleTypes(): NetworkResult<VehicleTypesResponse> =
        safeGet { url("$baseUrl/vehicle-types") }

    override suspend fun searchCars(
        query: String?,
        page: Int,
        limit: Int,
        typeId: String?,
    ): NetworkResult<SearchResponse> =
        safeGet {
            url("$baseUrl/search")
            parameter("page", page)
            parameter("limit", limit)
            if (!query.isNullOrBlank()) {
                parameter("q", query)
            }
            if (typeId != null) {
                // Advanced filter notation: filters[0].field=type&filters[0].op==&filters[0].value=<uuid>
                parameter("filters[0].field", "type")
                parameter("filters[0].op", "=")
                parameter("filters[0].value", typeId)
            }
        }

    override suspend fun getExpandedPrice(
        modelSlug: String,
        fuelAcronym: String,
        year: String,
    ): NetworkResult<ExpandedPriceResponse> =
        safeGet {
            url("$baseUrl/prices/expanded")
            parameter("model_slug", modelSlug)
            parameter("fuel_acronym", fuelAcronym)
            parameter("year", year)
        }

    private suspend inline fun <reified T> safeGet(block: HttpRequestBuilder.() -> Unit): NetworkResult<T> {
        return try {
            val response: HttpResponse = httpClient.get(block)
            android.util.Log.d("CarsApiImpl", "GET ${response.call.request.url} -> status=${response.status.value}")
            if (response.status.isSuccess()) {
                NetworkResult.Success(response.body())
            } else {
                android.util.Log.e(
                    "CarsApiImpl",
                    "Request failed code=" + response.status.value + " message=" + response.status.description,
                )
                NetworkResult.Error(code = response.status.value, message = response.status.description)
            }
        } catch (t: Throwable) {
            android.util.Log.e("CarsApiImpl", "Exception on GET: " + (t.message ?: "unknown"), t)
            NetworkResult.Error(message = t.message, throwable = t)
        }
    }
}

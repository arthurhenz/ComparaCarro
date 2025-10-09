package comparacarro.network.api

import comparacarro.network.model.BackendCar
import comparacarro.network.model.BackendCarDetail
import comparacarro.network.result.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class CarsApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : CarsApi {

    override suspend fun getCars(): NetworkResult<List<BackendCar>> {
        return safeGet("$baseUrl/cars")
    }

    override suspend fun getCarById(id: Int): NetworkResult<BackendCarDetail> {
        return safeGet("$baseUrl/cars/$id")
    }

    private suspend inline fun <reified T> safeGet(url: String): NetworkResult<T> {
        return try {
            val response: HttpResponse = httpClient.get(url)
            android.util.Log.d("CarsApiImpl", "GET " + url + " -> status=" + response.status.value)
            if (response.status.isSuccess()) {
                val body: T = response.body()
                android.util.Log.d("CarsApiImpl", "Parsed body type=" + (T::class.simpleName ?: "unknown"))
                NetworkResult.Success(body)
            } else {
                android.util.Log.e(
                    "CarsApiImpl",
                    "Request failed code=" + response.status.value + " message=" + response.status.description
                )
                NetworkResult.Error(code = response.status.value, message = response.status.description)
            }
        } catch (t: Throwable) {
            android.util.Log.e("CarsApiImpl", "Exception on GET " + url + ": " + (t.message ?: "unknown"), t)
            NetworkResult.Error(message = t.message, throwable = t)
        }
    }
}

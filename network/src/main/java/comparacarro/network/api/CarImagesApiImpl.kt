package comparacarro.network.api

import comparacarro.network.model.CarImageRequest
import comparacarro.network.model.SignedUrlsRequest
import comparacarro.network.model.SignedUrlsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class CarImagesApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val apiKey: String,
) : CarImagesApi {
    override suspend fun getSignedUrls(requests: List<CarImageRequest>): List<String?> {
        if (requests.isEmpty()) return emptyList()
        // carimagesapi caps a batch at 50 images per request.
        return requests.chunked(MAX_BATCH).flatMap { chunk -> signChunk(chunk) }
    }

    private suspend fun signChunk(chunk: List<CarImageRequest>): List<String?> {
        return try {
            val response: HttpResponse =
                httpClient.post("$baseUrl/api/v1/signed-urls") {
                    parameter("api_key", apiKey)
                    contentType(ContentType.Application.Json)
                    setBody(SignedUrlsRequest(chunk))
                }
            if (!response.status.isSuccess()) {
                android.util.Log.e("CarImagesApiImpl", "signed-urls failed status=${response.status.value}")
                return List(chunk.size) { null }
            }
            val urls = response.body<SignedUrlsResponse>().urls
            // Align with the request size so callers can zip by index.
            List(chunk.size) { i -> urls.getOrNull(i) }
        } catch (t: Throwable) {
            android.util.Log.e("CarImagesApiImpl", "signed-urls error: ${t.message}", t)
            List(chunk.size) { null }
        }
    }

    private companion object {
        const val MAX_BATCH = 50
    }
}

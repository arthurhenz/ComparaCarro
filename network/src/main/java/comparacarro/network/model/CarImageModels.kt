package comparacarro.network.model

import kotlinx.serialization.Serializable

/*
 * Models for the carimagesapi.com signed-URL endpoint.
 *
 * POST /api/v1/signed-urls?api_key=ci_xxx
 *   body:     { "images": [ { make, model, year, width, format }, ... ] }  (max 50)
 *   response: { "urls": [ "<signed image url>" | null, ... ] }
 *
 * Signing happens server-side, so only the public api_key travels with the request —
 * no secret is ever sent from the client. Returned URLs are short-lived (expire same day).
 */

@Serializable
data class SignedUrlsRequest(
    val images: List<CarImageRequest>,
)

@Serializable
data class CarImageRequest(
    val make: String,
    val model: String? = null,
    val year: Int? = null,
    val width: Int? = null,
    val format: String = "webp",
)

@Serializable
data class SignedUrlsResponse(
    val urls: List<String?> = emptyList(),
)

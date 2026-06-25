package comparacarro.network.api

import comparacarro.network.model.CarImageRequest

interface CarImagesApi {
    /**
     * Resolves a batch of car-image requests into signed, ready-to-load image URLs
     * (carimagesapi.com `/api/v1/signed-urls`). The returned list is positionally
     * aligned with [requests]; an entry is null when no image could be resolved.
     * Never throws — failures yield nulls so callers fall back to a placeholder.
     */
    suspend fun getSignedUrls(requests: List<CarImageRequest>): List<String?>
}

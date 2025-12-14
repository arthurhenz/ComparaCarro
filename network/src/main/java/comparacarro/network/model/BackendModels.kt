package comparacarro2.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("page") val page: Int,
    @SerialName("pageSize") val pageSize: Int,
    @SerialName("totalItems") val totalItems: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("hasNext") val hasNext: Boolean,
    @SerialName("hasPrevious") val hasPrevious: Boolean
)

@Serializable
data class BackendCar(
    @SerialName("id") val id: Int,
    @SerialName("codigoFipe") val codigoFipe: String = "",
    @SerialName("marca") val marca: String = "",
    @SerialName("modelo") val modelo: String = "",
    @SerialName("anoModelo") val anoModelo: Int = 0,
    @SerialName("mesReferencia") val mesReferencia: Int = 0,
    @SerialName("anoReferencia") val anoReferencia: Int = 0,
    @SerialName("valor") val valor: Double = 0.0
)

@Serializable
data class BackendCarDetail(
    @SerialName("id") val id: Int,
    @SerialName("codigoFipe") val codigoFipe: String = "",
    @SerialName("marca") val marca: String = "",
    @SerialName("modelo") val modelo: String = "",
    @SerialName("anoModelo") val anoModelo: Int = 0,
    @SerialName("mesReferencia") val mesReferencia: Int = 0,
    @SerialName("anoReferencia") val anoReferencia: Int = 0,
    @SerialName("valor") val valor: Double = 0.0
)


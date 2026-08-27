package comparacarro.network.model

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
    @SerialName("hasPrevious") val hasPrevious: Boolean,
)

@Serializable
data class BackendCar(
    @SerialName("id") val id: Int,
    @SerialName("codigo_fipe") val codigoFipe: String = "",
    @SerialName("tipo_veiculo") val tipoVeiculo: String = "",
    @SerialName("nome_marca") val nomeMarca: String = "",
    @SerialName("nome_modelo") val nomeModelo: String = "",
    @SerialName("ano_modelo") val anoModelo: Int? = null,
    @SerialName("ano_referencia") val anoReferencia: Int = 0,
    @SerialName("mes_referencia") val mesReferencia: Int = 0,
    @SerialName("valor_centavos") val valorCentavos: Long = 0,
    @SerialName("valor_formatado") val valorFormatado: String = "",
    @SerialName("nome_combustivel") val nomeCombustivel: String = "",
    @SerialName("sigla_combustivel") val siglaCombustivel: String = "",
    @SerialName("zero_km") val zeroKm: Boolean = false,
)

@Serializable
data class BackendCarDetail(
    @SerialName("id") val id: Int,
    @SerialName("codigo_fipe") val codigoFipe: String = "",
    @SerialName("tipo_veiculo") val tipoVeiculo: String = "",
    @SerialName("nome_marca") val nomeMarca: String = "",
    @SerialName("nome_modelo") val nomeModelo: String = "",
    @SerialName("ano_modelo") val anoModelo: Int? = null,
    @SerialName("ano_referencia") val anoReferencia: Int = 0,
    @SerialName("mes_referencia") val mesReferencia: Int = 0,
    @SerialName("valor_centavos") val valorCentavos: Long = 0,
    @SerialName("valor_formatado") val valorFormatado: String = "",
    @SerialName("nome_combustivel") val nomeCombustivel: String = "",
    @SerialName("sigla_combustivel") val siglaCombustivel: String = "",
    @SerialName("zero_km") val zeroKm: Boolean = false,
)

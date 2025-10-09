package comparacarro.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendCar(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("category") val category: BackendCarCategory,
    @SerialName("fipe") val fipe: Float,
    @SerialName("views") val views: Int,
    @SerialName("opcionais") val opcionais: List<BackendCarOptionals> = emptyList()
)

@Serializable
data class BackendCarDetail(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("category") val category: BackendCarCategory,
    @SerialName("fipe") val fipe: Float,
    @SerialName("views") val views: Int,
    @SerialName("opcionais") val opcionais: List<BackendCarOptionals> = emptyList()
)

@Serializable
enum class BackendCarCategory {
    SEDAN,
    SUV,
    HATCHBACK,
    LUXURY,
    PICKUP,
    SPORTS,
    ELECTRIC
}

@Serializable
enum class BackendCarOptionals {
    @SerialName("BANCO_COURO") BANCO_COURO,
    @SerialName("TETO_SOLAR") TETO_SOLAR
}




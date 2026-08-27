package com.data.model

fun fuelLabel(car: CarDetailData): String {
    val name = car.fuelName
    val acronym = car.fuelAcronym
    return when {
        name.isNotBlank() && acronym.isNotBlank() -> "$name ($acronym)"
        name.isNotBlank() -> name
        else -> "—"
    }
}

fun formatPct(value: Double?): String = value?.let { "%+.2f%%".format(it * 100) } ?: "—"

fun orDash(value: String?): String = if (value.isNullOrBlank()) "—" else value

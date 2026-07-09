package com.favorites

/** Preset FIPE price buckets, in reais. [maxReais] is exclusive; [ABOVE_200K] has no upper bound. */
enum class PriceRange(val label: String, val minReais: Long, val maxReais: Long) {
    UP_TO_50K("Até R$ 50 mil", 0, 50_000),
    FROM_50_TO_100K("R$ 50–100 mil", 50_000, 100_000),
    FROM_100_TO_200K("R$ 100–200 mil", 100_000, 200_000),
    ABOVE_200K("Acima de R$ 200 mil", 200_000, Long.MAX_VALUE),
    ;

    fun contains(valueReais: Long): Boolean = valueReais in minReais until maxReais
}

/**
 * The active favorites filter. A `null` facet means "no restriction"; multiple facets combine with
 * AND. [isActive] drives the empty-state copy (no matches vs. no favorites at all).
 */
data class FavoriteFilter(
    val brand: String? = null,
    val priceRange: PriceRange? = null,
    val year: String? = null,
) {
    val isActive: Boolean get() = brand != null || priceRange != null || year != null
}

/**
 * The values still selectable in each chip, recomputed as a faceted set: every list holds only the
 * values that exist among favorites matching the *other* active filters. Selecting a brand narrows
 * [years] and [priceRanges] to that brand, changing the price narrows [years] again, and so on.
 */
data class FavoriteFilterOptions(
    val brands: List<String> = emptyList(),
    val years: List<String> = emptyList(),
    val priceRanges: List<PriceRange> = emptyList(),
)

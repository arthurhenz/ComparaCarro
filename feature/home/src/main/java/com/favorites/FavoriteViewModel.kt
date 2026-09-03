package com.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.common.utils.stateInWhileSubscribed
import com.data.model.FavoriteCar
import com.data.usecase.GetFavoritesUseCase
import com.data.usecase.ObserveAllFavoritesUseCase
import com.data.usecase.RemoveFavoriteUseCase
import com.navigation.routes.SelectComparisonRoute
import com.navigation.routes.navigateToDetail
import com.navigation.routes.parseVehicleSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    observeAllFavoritesUseCase: ObserveAllFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _filter = MutableStateFlow(FavoriteFilter())
    val filter: StateFlow<FavoriteFilter> = _filter.asStateFlow()

    // Cache the full mapped feed once, then re-derive the filtered view whenever the filter changes
    // (filtering after cachedIn keeps paging intact and avoids re-querying Room on every chip tap).
    val favorites: Flow<PagingData<FavoriteCarItem>> =
        getFavoritesUseCase()
            .map { page -> page.map(FavoriteCar::toItem) }
            .cachedIn(viewModelScope)
            .combine(_filter) { paging, filter -> paging.filter { filter.matches(it) } }

    // The whole stored set, reduced to just the facet fields, so options can be recomputed cheaply.
    private val facetCars: Flow<List<FacetCar>> =
        observeAllFavoritesUseCase().map { list -> list.map(FavoriteCar::toFacet) }

    // Total favorites count, known from Room even before the paged list finishes its first load —
    // lets the loading skeleton show as many bones as there are real cards instead of a fixed number.
    val favoritesCount: StateFlow<Int> =
        facetCars.map { it.size }.stateInWhileSubscribed(viewModelScope, 0)

    // Faceted chip options: each list reacts to the filter, holding only values available given the
    // *other* active facets. Selecting a brand narrows the year/price chips, and vice versa.
    val filterOptions: StateFlow<FavoriteFilterOptions> =
        combine(facetCars, _filter) { cars, filter -> buildOptions(cars, filter) }
            .stateInWhileSubscribed(viewModelScope, FavoriteFilterOptions())

    fun onBrandSelected(brand: String?) = _filter.update { it.copy(brand = brand) }

    fun onPriceRangeSelected(priceRange: PriceRange?) = _filter.update { it.copy(priceRange = priceRange) }

    fun onYearSelected(year: String?) = _filter.update { it.copy(year = year) }

    fun clearFilters() = _filter.update { FavoriteFilter() }

    fun onEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.RemoveFavorite -> removeFavorite(event.id)
        }
    }

    private fun removeFavorite(id: String) =
        viewModelScope.launch {
            try {
                removeFavoriteUseCase(id)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Failed to remove favorite $id: ${e.message}", e)
            }
        }

    fun openDetail(id: String) = navigateToDetail(id)

    fun navigateToCompare() {
        navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
    }
}

private fun FavoriteFilter.matches(item: FavoriteCarItem): Boolean {
    if (brand != null && !item.brand.equals(brand, ignoreCase = true)) return false
    if (year != null && parseVehicleSpec(item.id).third != year) return false
    if (priceRange != null) {
        val value = parsePriceReais(item.price) ?: return false
        if (!priceRange.contains(value)) return false
    }
    return true
}

/** Parses a FIPE label like "R$ 187.990" or "R$ 1.780,00" into whole reais (centavos dropped). */
private fun parsePriceReais(price: String): Long? =
    price.substringBefore(",").filter { it.isDigit() }.toLongOrNull()

/** The favorite reduced to just what the filter chips need. */
private data class FacetCar(val brand: String, val year: String, val priceReais: Long?)

private fun FavoriteCar.toFacet(): FacetCar =
    FacetCar(
        brand = brand,
        year = parseVehicleSpec(id).third,
        priceReais = parsePriceReais(price),
    )

/**
 * Faceted options: for each chip keep only values present among favorites that satisfy every *other*
 * active filter (the chip's own selection is ignored so it stays switchable). This is what makes the
 * chips cascade — pick a brand and the year/price chips shrink to that brand's cars, and so on.
 */
private fun buildOptions(cars: List<FacetCar>, filter: FavoriteFilter): FavoriteFilterOptions {
    fun matchesBrand(c: FacetCar) = filter.brand == null || c.brand.equals(filter.brand, ignoreCase = true)
    fun matchesYear(c: FacetCar) = filter.year == null || c.year == filter.year
    fun matchesPrice(c: FacetCar) =
        filter.priceRange == null || (c.priceReais != null && filter.priceRange.contains(c.priceReais))

    val brands =
        cars.filter { matchesYear(it) && matchesPrice(it) }
            .map { it.brand }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    val years =
        cars.filter { matchesBrand(it) && matchesPrice(it) }
            .map { it.year }
            .filter { it.isNotBlank() }
            .distinct()
            .sortedDescending()
    val priceRanges =
        PriceRange.entries.filter { range ->
            cars.any {
                matchesBrand(it) && matchesYear(it) && it.priceReais != null && range.contains(it.priceReais)
            }
        }
    return FavoriteFilterOptions(brands = brands, years = years, priceRanges = priceRanges)
}

private fun FavoriteCar.toItem(): FavoriteCarItem =
    FavoriteCarItem(
        id = id,
        brand = brand,
        title = title,
        price = price,
        powertrain = powertrain,
        range = range,
        imageUrl = imageUrl,
    )

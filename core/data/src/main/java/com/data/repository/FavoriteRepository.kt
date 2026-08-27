package com.data.repository

import androidx.paging.PagingData
import com.data.model.FavoriteCar
import kotlinx.coroutines.flow.Flow

/**
 * Access to the user's favorite cars, backed directly by Room (the single source of truth).
 *
 * [favorites] is a paginated feed (30 per page) for the Favorites list. Membership is exposed
 * separately via [observeFavoriteIds] / [observeIsFavorite] because a [PagingData] stream can't be
 * enumerated into the full id set the home grid and detail heart need. All of these are live Room
 * queries, so writes ([add] / [remove] / [toggle]) show up in observers without any manual cache.
 */
interface FavoriteRepository {
    /** The favorites feed, newest first, one 30-item page at a time. */
    val favorites: Flow<PagingData<FavoriteCar>>

    /** The ids of every favorite, for aggregate membership checks (e.g. the home grid hearts). */
    fun observeFavoriteIds(): Flow<Set<String>>

    /** The full favorites set as a live list, for computing the Favorites filter facets. */
    fun observeAll(): Flow<List<FavoriteCar>>

    /** Reactive membership check for a single car id. */
    fun observeIsFavorite(id: String): Flow<Boolean>

    suspend fun add(car: FavoriteCar)

    suspend fun remove(id: String)

    /** Adds the car if absent, removes it if present. Returns the new favorited state. */
    suspend fun toggle(car: FavoriteCar): Boolean
}

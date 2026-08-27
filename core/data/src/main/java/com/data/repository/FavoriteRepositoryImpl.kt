package com.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.data.local.FavoriteCarDao
import com.data.local.toDomain
import com.data.local.toEntity
import com.data.model.FavoriteCar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

/**
 * Room-backed favorites store. Room is the single source of truth; there is no in-memory cache.
 *
 * The list is served through Paging 3: [favorites] wraps the DAO's `PagingSource` in a [Pager] that
 * loads [PAGE_SIZE] rows per page and re-queries automatically whenever the table changes. Since a
 * page stream can't be enumerated into the full favorited-id set, membership is served by a separate
 * live `observeIds` query mapped into [observeFavoriteIds] / [observeIsFavorite]. Writes go straight
 * to Room and propagate to all of the above through those live queries.
 */
@Single
class FavoriteRepositoryImpl(
    private val favoriteCarDao: FavoriteCarDao,
) : FavoriteRepository {
    override val favorites: Flow<PagingData<FavoriteCar>> =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) { favoriteCarDao.getAll() }
            .flow
            .map { page -> page.map { it.toDomain() } }

    override fun observeFavoriteIds(): Flow<Set<String>> =
        favoriteCarDao.observeIds()
            .map { it.toSet() }
            .distinctUntilChanged()

    override fun observeAll(): Flow<List<FavoriteCar>> =
        favoriteCarDao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .distinctUntilChanged()

    override fun observeIsFavorite(id: String): Flow<Boolean> =
        observeFavoriteIds()
            .map { id in it }
            .distinctUntilChanged()

    override suspend fun add(car: FavoriteCar) {
        favoriteCarDao.upsert(car.toEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun remove(id: String) {
        favoriteCarDao.deleteById(id)
    }

    override suspend fun toggle(car: FavoriteCar): Boolean =
        if (favoriteCarDao.exists(car.id)) {
            remove(car.id)
            false
        } else {
            add(car)
            true
        }

    private companion object {
        // Favorites are paged 30 at a time.
        const val PAGE_SIZE = 30
    }
}

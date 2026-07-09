package com.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCarDao {
    /**
     * The favorites feed as a Paging [PagingSource], newest first. Paging pulls one page at a time
     * (30 rows — see the repository's `PagingConfig`) and rebinds this source automatically whenever
     * the table changes, so the list stays live without an in-memory cache.
     */
    @Query("SELECT * FROM favorite_cars ORDER BY addedAt DESC")
    fun getAll(): PagingSource<Int, FavoriteCarEntity>

    /**
     * Observes just the ids of every favorite. This is the lightweight membership signal the
     * paginated [getAll] can't provide (a page stream isn't enumerable): the repository turns it
     * into the "is this car favorited?" flows used by the home grid and the detail heart.
     */
    @Query("SELECT id FROM favorite_cars")
    fun observeIds(): Flow<List<String>>

    /**
     * The full favorites set as a live list (newest first). Used to compute the Favorites filter
     * facets (brand/price/year), where each chip's options depend on the other active filters, so a
     * paged source won't do — the whole set must be enumerable. Safe because it's a personal list.
     */
    @Query("SELECT * FROM favorite_cars ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteCarEntity>>

    /** One-shot membership check used by `toggle` to decide between add and remove. */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cars WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(car: FavoriteCarEntity)

    @Query("DELETE FROM favorite_cars WHERE id = :id")
    suspend fun deleteById(id: String)
}

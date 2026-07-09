package com.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The app's Room database. Currently holds only favorites; new entities/DAOs are added here.
 *
 * `exportSchema = false` keeps the setup schema-less for now. Turn it on (and provide a schema
 * location + migrations) before shipping a change that alters the schema in production.
 */
@Database(
    entities = [FavoriteCarEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ComparaCarroDatabase : RoomDatabase() {
    abstract fun favoriteCarDao(): FavoriteCarDao

    companion object {
        const val NAME = "comparacarro.db"
    }
}

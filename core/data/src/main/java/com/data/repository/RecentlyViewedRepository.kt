package com.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recently_viewed")

@Single
class RecentlyViewedRepository(
    private val context: Context
) {
    private val RECENTLY_VIEWED_KEY = stringPreferencesKey("recently_viewed_car_ids")
    private val MAX_RECENT_ITEMS = 5

    suspend fun addRecentlyViewedCarId(carId: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[RECENTLY_VIEWED_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
            val newList = mutableListOf(carId)
            
            // Add existing items, excluding the current carId to avoid duplicates
            currentList.filter { it != carId }.forEach { existingId ->
                if (newList.size < MAX_RECENT_ITEMS) {
                    newList.add(existingId)
                }
            }
            
            preferences[RECENTLY_VIEWED_KEY] = newList.joinToString(",")
        }
    }

    fun getRecentlyViewedCarIds(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[RECENTLY_VIEWED_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }

    suspend fun getRecentlyViewedCarIdsSync(): List<String> {
        return getRecentlyViewedCarIds().first()
    }

}


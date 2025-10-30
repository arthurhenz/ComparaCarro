package com.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.common.utils.Constants.MAX_RECENT_ITEMS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recently_viewed")
private val RECENTLY_VIEWED_KEY = stringPreferencesKey("recently_viewed_car_ids")

@Single
class RecentlyViewedRepository(
    private val context: Context
) {

    suspend fun addRecentlyViewedCarId(carId: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[RECENTLY_VIEWED_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
            val newList = mutableListOf(carId)

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

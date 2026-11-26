package com.example.movielibraryproject.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "favorites_preferences"
)

/** The ordering used to display saved movies. */
enum class FavoritesSortOrder {
    TITLE,
    RECENTLY_ADDED
}

class FavoritesPreferencesRepository(context: Context) {
    private val dataStore = context.applicationContext.favoritesDataStore

    val sortOrder: Flow<FavoritesSortOrder> = dataStore.data.map { preferences ->
        preferences[SORT_ORDER_KEY]
            ?.let { storedValue ->
                FavoritesSortOrder.entries.firstOrNull { it.name == storedValue }
            }
            ?: DEFAULT_SORT_ORDER
    }

    suspend fun setSortOrder(sortOrder: FavoritesSortOrder) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder.name
        }
    }

    private companion object {
        val SORT_ORDER_KEY = stringPreferencesKey("favorites_sort_order")
        val DEFAULT_SORT_ORDER = FavoritesSortOrder.TITLE
    }
}

package com.example.weatherapp.data.locale

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStorageManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val PREFERENCES_STORE_NAME = "searchHistoryStore"
    }
    
    suspend fun saveItemToPrefs(item: String) {
        dataStore.edit { prefs ->
            if (prefs.contains(intPreferencesKey(item)))
                prefs.remove(longPreferencesKey(item))
            prefs[longPreferencesKey(item)] = System.currentTimeMillis()
        }
    }
    
    suspend fun deleteItemFromPrefs(item: String) {
        dataStore.edit {prefs ->
            prefs.remove(longPreferencesKey(item))
        }
    }
    
    suspend fun clearPrefs() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    
    fun observePrefs(): Flow<Preferences> {
        return dataStore.data
    }
}
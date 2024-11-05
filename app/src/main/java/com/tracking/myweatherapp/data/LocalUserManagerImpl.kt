package com.tracking.myweatherapp.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tracking.myweatherapp.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserManagerImpl @Inject constructor(
    private val application: Application
) : LocalUserManger {

    override suspend fun saveSearchQuery(query: String) {
        application.dataStore.edit { settings ->
            Log.d("LocalUserMangerImpl", "saveSearchQuery: $query")
            settings[PreferenceKeys.SEARCH_QUERY] = query
        }
    }

    override fun readSearchQuery(): Flow<String> {
        return application.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.SEARCH_QUERY] ?: ""
        }
    }
}

private val readOnlyProperty =
    preferencesDataStore(name = Constants.USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val SEARCH_QUERY = stringPreferencesKey(Constants.SEARCH_QUERY)
}
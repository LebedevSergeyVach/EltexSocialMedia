package com.eltex.androidschool.store

import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eltex.androidschool.utils.Logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    val authTokenFlow: Flow<String?> = dataStore.data
        .map { preferences: Preferences ->
            preferences[AUTH_TOKEN_KEY]
        }

    val userIdFlow: Flow<String?> = dataStore.data
        .map { preferences: Preferences ->
            preferences[USER_ID_KEY]
        }

    suspend fun saveUserCredentials(authToken: String, userId: String) {
        Logger.e("UserPreferences: userId = $userId")

        dataStore.edit { preferences: MutablePreferences ->
            preferences[AUTH_TOKEN_KEY] = authToken
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }
    }
}

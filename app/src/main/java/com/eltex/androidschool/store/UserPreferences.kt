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

/**
 * Класс для работы с настройками пользователя, хранящимися в [DataStore].
 * Предоставляет методы для сохранения и получения данных аутентификации (токен и идентификатор пользователя).
 *
 * @property dataStore Экземпляр [DataStore], используемый для хранения настроек.
 */
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        /**
         * Ключ для хранения токена аутентификации в [DataStore].
         */
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

        /**
         * Ключ для хранения идентификатора пользователя в [DataStore].
         */
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    /**
     * [Flow], который предоставляет токен аутентификации.
     * Если токен отсутствует, возвращает `null`.
     *
     * @return [Flow], который эмитит токен аутентификации или `null`.
     *
     * @see Flow
     */
    val authTokenFlow: Flow<String?> = dataStore.data
        .map { preferences: Preferences ->
            preferences[AUTH_TOKEN_KEY]
        }

    /**
     * [Flow], который предоставляет идентификатор пользователя.
     * Если идентификатор отсутствует, возвращает `null`.
     *
     * @return [Flow], который эмитит идентификатор пользователя или `null`.
     *
     * @see Flow
     */
    val userIdFlow: Flow<String?> = dataStore.data
        .map { preferences: Preferences ->
            preferences[USER_ID_KEY]
        }

    /**
     * Сохраняет данные аутентификации (токен и идентификатор пользователя) в [DataStore].
     *
     * @param authToken Токен аутентификации.
     * @param userId Идентификатор пользователя.
     */
    suspend fun saveUserCredentials(authToken: String, userId: String) {
        Logger.e("UserPreferences: userId = $userId")

        dataStore.edit { preferences: MutablePreferences ->
            preferences[AUTH_TOKEN_KEY] = authToken
            preferences[USER_ID_KEY] = userId
        }
    }

    /**
     * Очищает данные аутентификации (токен и идентификатор пользователя) из [DataStore].
     */
    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }
    }
}

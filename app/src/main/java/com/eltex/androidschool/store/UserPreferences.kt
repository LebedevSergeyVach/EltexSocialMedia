package com.eltex.androidschool.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

/**
 * Менеджер пользовательских предпочтений, который отвечает за сохранение и получение
 * токена аутентификации и идентификатора пользователя, используя DataStore.
 *
 * @property dataStore Объект [DataStore], который используется для хранения и
 * получения пользовательских предпочтений.
 *
 * @constructor Создает экземпляр [UserPreferences] с указанным [dataStore].
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
     * Сохраняет учетные данные пользователя (токен аутентификации и идентификатор пользователя)
     * в [DataStore].
     *
     * Это приостанавливающая функция, которая принимает токен аутентификации и
     * идентификатор пользователя и сохраняет их в предпочтениях.
     *
     * @param authToken Токен аутентификации, который будет сохранен.
     * @param userId Идентификатор пользователя, который будет сохранен.
     * @throws Exception Если возникает ошибка при сохранении данных.
     * @see dataStore
     */
    suspend fun saveUserCredentials(authToken: String, userId: String) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[AUTH_TOKEN_KEY] = authToken
            preferences[USER_ID_KEY] = userId
        }
    }

    /**
     * Очищает данные аутентификации, удаляя токен аутентификации и идентификатор пользователя
     * из [DataStore].
     *
     * Это приостанавливающая функция, которая не принимает параметров.
     *
     * @throws Exception Если возникает ошибка при удалении данных.
     * @see dataStore
     */
    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
        }
    }
}

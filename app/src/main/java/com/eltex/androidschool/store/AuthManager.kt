package com.eltex.androidschool.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Менеджер аутентификации, который отвечает за управление токеном аутентификации
 * и идентификатором пользователя, используя предпочтения пользователя.
 *
 * @property userPreferences Объект [UserPreferences], который используется для
 * получения токена аутентификации и идентификатора пользователя.
 *
 * @constructor Создает экземпляр [AuthManager] с указанным [userPreferences].
 */
@Singleton
class AuthManager @Inject constructor(
    private val userPreferences: UserPreferences
) {
    /**
     * Поток, который предоставляет токен аутентификации.
     *
     * @see userPreferences.authTokenFlow
     */
    val authTokenFlow: Flow<String?> = userPreferences.authTokenFlow

    /**
     * Поток, который предоставляет идентификатор пользователя.
     *
     * @see userPreferences.userIdFlow
     */
    val userIdFlow: Flow<String?> = userPreferences.userIdFlow

    /**
     * Получает токен аутентификации из предпочтений пользователя.
     *
     * Это приостанавливающая функция, которая возвращает токен аутентификации или null,
     * если токен не установлен.
     *
     * @return Токен аутентификации или null, если токен не установлен.
     * @throws Exception Если возникает ошибка при получении токена.
     * @see userPreferences.authTokenFlow
     */
    suspend fun getAuthToken(): String? {
        return userPreferences.authTokenFlow.first()
    }

    /**
     * Получает идентификатор пользователя из предпочтений пользователя.
     *
     * Это приостанавливающая функция, которая возвращает идентификатор пользователя
     * в виде [Long] или null, если идентификатор не установлен.
     *
     * @return Идентификатор пользователя в виде [Long] или null, если идентификатор не установлен.
     * @throws Exception Если возникает ошибка при получении идентификатора.
     * @see userPreferences.userIdFlow
     */
    suspend fun getUserId(): Long? {
        return userPreferences.userIdFlow.first()?.toLong()
    }
}

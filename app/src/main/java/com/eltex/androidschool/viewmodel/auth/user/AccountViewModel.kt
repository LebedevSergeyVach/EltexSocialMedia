package com.eltex.androidschool.viewmodel.auth.user

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.store.UserPreferences

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import okio.IOException

import javax.inject.Inject

/**
 * ViewModel для работы с данными аккаунта пользователя.
 * Предоставляет доступ к токену аутентификации и идентификатору пользователя.
 *
 * @property userPreferences Хранилище для работы с данными пользователя.
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    /**
     * [Flow], который предоставляет токен аутентификации.
     * Если токен отсутствует, возвращает `null`.
     *
     * @return [Flow], который эмитит токен аутентификации или `null`.
     *
     * @see Flow
     */
    val authTokenFlow: Flow<String?> = userPreferences.authTokenFlow

    /**
     * [Flow], который предоставляет идентификатор пользователя.
     * Если идентификатор отсутствует, возвращает `null`.
     *
     * @return [Flow], который эмитит идентификатор пользователя или `null`.
     *
     * @see Flow
     */
    val userIdFlow: Flow<String?> = userPreferences.userIdFlow

    /**
     * Получает токен аутентификации.
     *
     * @return Токен аутентификации или `null`, если токен отсутствует.
     *
     * @throws IOException Если произошла ошибка при получении токена.
     */
    private suspend fun getAuthToken(): String? {
        return userPreferences.authTokenFlow.first()
    }

    /**
     * Получает идентификатор пользователя.
     *
     * @return Идентификатор пользователя или `null`, если идентификатор отсутствует.
     *
     * @throws IOException Если произошла ошибка при получении идентификатора.
     */
    private suspend fun getUserId(): Long? {
        return userPreferences.userIdFlow.first()?.toLong()
    }

    /**
     * Идентификатор пользователя.
     *
     * @throws IOException Если идентификатор пользователя не найден.
     */
    val userId: Long = runBlocking { getUserId() } ?: throw IOException("USER ID NOT FOUND")
}

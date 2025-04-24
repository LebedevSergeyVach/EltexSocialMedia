package com.eltex.androidschool.repository.auth

import android.content.ContentResolver

import com.eltex.androidschool.data.auth.AuthData
import com.eltex.androidschool.data.media.FileModel

/**
 * Интерфейс репозитория для работы с аутентификацией и регистрацией.
 * Определяет методы для входа в систему и регистрации нового пользователя.
 */
interface AuthRepository {

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @see AuthData
     */
    suspend fun login(login: String, password: String): AuthData

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param login Логин пользователя.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param fileModel Модель файла аватара пользователя. Может быть `null`, если файл не загружен.
     * @param contentResolver [ContentResolver] для работы с файлом.
     * @param onProgress Колбэк для отслеживания прогресса загрузки файла.
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @see AuthData
     * @see FileModel
     */
    suspend fun register(
        login: String,
        username: String,
        password: String,
        fileModel: FileModel?,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit,
    ): AuthData

    fun getProhibitedUsernamesAndNicknames(): List<String>
}

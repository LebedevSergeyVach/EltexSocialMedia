package com.eltex.androidschool.api.auth

import com.eltex.androidschool.data.auth.AuthData
import okhttp3.MultipartBody
import retrofit2.http.Multipart

import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Интерфейс для работы с API аутентификации и регистрации.
 * Определяет методы для входа в систему и регистрации нового пользователя.
 */
interface AuthApi {

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @see AuthData
     */
    @POST("api/users/authentication")
    suspend fun login(
        @Query("login") login: String,
        @Query("pass") password: String,
    ): AuthData

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @param name Имя пользователя.
     * @param file Файл аватара пользователя в формате [MultipartBody.Part].
     * @return Объект [AuthData], содержащий токен аутентификации и идентификатор пользователя.
     *
     * @see AuthData
     * @see MultipartBody.Part
     */
    @Multipart
    @POST("api/users/registration")
    suspend fun register(
        @Query("login") login: String,
        @Query("pass") password: String,
        @Query("name") name: String,
        @Part file: MultipartBody.Part,
    ): AuthData
}

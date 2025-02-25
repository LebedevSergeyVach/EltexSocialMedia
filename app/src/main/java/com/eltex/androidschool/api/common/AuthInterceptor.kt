package com.eltex.androidschool.api.common

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.store.UserPreferences

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import okhttp3.Interceptor
import okhttp3.Response

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Перехватчик (Interceptor) для добавления заголовков аутентификации в сетевые запросы.
 * Добавляет API-ключ и токен аутентификации (если он доступен) в каждый запрос.
 *
 * @property userPreferences Хранилище для получения токена аутентификации.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    private companion object {
        /**
         * Название заголовка для API-ключа.
         */
        private const val API_KEY = "Api-Key"

        /**
         * Название заголовка для токена аутентификации.
         */
        private const val API_AUTHORIZATION = "Authorization"
    }

    /**
     * Перехватывает запрос и добавляет необходимые заголовки.
     *
     * @param chain Цепочка запросов, предоставляющая доступ к текущему запросу и возможность его модификации.
     * @return Ответ на запрос после его выполнения.
     *
     * @throws IOException Если произошла ошибка при выполнении запроса.
     * @see Interceptor
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken: String? = runBlocking { userPreferences.authTokenFlow.first() }

        val newRequest = if (authToken.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader(API_KEY, BuildConfig.API_KEY)
                .build()
        } else {
            chain.request().newBuilder()
                .addHeader(API_KEY, BuildConfig.API_KEY)
                .addHeader(API_AUTHORIZATION, authToken)
                .build()
        }

        // Продолжаем выполнение запроса
        return chain.proceed(newRequest)
    }
}

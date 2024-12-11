package com.eltex.androidschool.api.posts

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Фабрика для создания экземпляра Retrofit.
 *
 * Этот объект отвечает за настройку и создание экземпляра Retrofit, который используется для выполнения сетевых запросов.
 * Retrofit настроен с использованием OkHttpClient и конвертера для работы с JSON.
 */
object RetrofitFactoryPost {
    private val JSON_TYPE = "application/json".toMediaType()
    private const val API_BASE_URL_POST = "https://eltex-android.ru/"

    /**
     * Конфигурация JSON-сериализации.
     *
     * - `ignoreUnknownKeys = true`: Игнорирует неизвестные ключи в JSON.
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Экземпляр Retrofit, настроенный для работы с API постов.
     *
     * Ленивая инициализация, чтобы создать экземпляр Retrofit только при первом обращении.
     */
    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClientFactoryPost.INSTANCE)
            .baseUrl(API_BASE_URL_POST)
            .addConverterFactory(json.asConverterFactory(JSON_TYPE))
            .build()
    }
}

package com.eltex.androidschool.api.common

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

/**
 * Фабрика для создания экземпляра Retrofit.
 *
 * Этот объект отвечает за настройку и создание экземпляра Retrofit, который используется для выполнения сетевых запросов.
 * Retrofit настроен с использованием OkHttpClient и конвертера для работы с JSON.
 *
 * @see Retrofit Основной класс для работы с сетевыми запросами.
 * @see RxJava3CallAdapterFactory Адаптер для интеграции RxJava3 с Retrofit.
 * @see Json Конфигурация JSON-сериализации.
 */
object RetrofitFactory {
    private val JSON_TYPE = "application/json".toMediaType()
    private const val API_BASE_URL = "https://eltex-android.ru/"

    /**
     * Конфигурация JSON-сериализации.
     *
     * - `ignoreUnknownKeys = true`: Игнорирует неизвестные ключи в JSON.
     * - `coerceInputValues = true`: Приводит значения `null` к значениям по умолчанию.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Ленивая инициализация экземпляра Retrofit.
     * Настраивает базовый URL, конвертер JSON и клиент OkHttp.
     *
     * @return Настроенный экземпляр Retrofit.
     * @see [OkHttpClientFactory]
     */
    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(json.asConverterFactory(JSON_TYPE))
            .build()
}

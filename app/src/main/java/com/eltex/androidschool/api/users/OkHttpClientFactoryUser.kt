package com.eltex.androidschool.api.users

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.utils.DnsSelector

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.util.concurrent.TimeUnit

/**
 * Фабрика для создания экземпляра OkHttpClient.
 *
 * Этот объект отвечает за настройку и создание экземпляра OkHttpClient, который используется для выполнения сетевых запросов.
 * OkHttpClient настроен с использованием интерсепторов для добавления заголовков и логирования.
 */
object OkHttpClientFactoryUser {
    private const val API_KEY = "Api-Key"
    private const val API_AUTHORIZATION = "Authorization"

    /**
     * Экземпляр OkHttpClient, настроенный для работы с API, настроенный для выполнения HTTP-запросов.
     *
     * - Установлено время ожидания соединения в 30 секунд.
     * - В режиме отладки (BuildConfig.DEBUG) добавлен интерсептор [HttpLoggingInterceptor] для логирования тела запросов и ответов.
     * - Добавлен интерсептор для установки заголовков [API_KEY] и [API_AUTHORIZATION] на основе значений из [BuildConfig].
     * - Используется пользовательская DNS-реализация [DnsSelector].
     *
     * Ленивая инициализация, чтобы создать экземпляр OkHttpClient только при первом обращении.
     * Этот клиент используется для выполнения сетевых запросов к API.
     */
    val INSTANCE: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .let { clientOkHttp: OkHttpClient.Builder ->
                if (BuildConfig.DEBUG) {
                    clientOkHttp.addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                } else {
                    clientOkHttp
                }
            }
            .addInterceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(API_KEY, BuildConfig.API_KEY)
                        .addHeader(API_AUTHORIZATION, BuildConfig.AUTHORIZATION)
                        .build()
                )
            }
            .dns(DnsSelector())
            .build()
    }
}

package com.eltex.androidschool.di

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.api.auth.AuthApi
import com.eltex.androidschool.api.common.AuthInterceptor
import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.api.events.EventsApi
import com.eltex.androidschool.api.users.UsersApi
import com.eltex.androidschool.api.jobs.JobsApi
import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.store.UserPreferences
import com.eltex.androidschool.utils.helper.DnsSelectorHelper

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import kotlinx.serialization.json.Json

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

/**
 * Модуль Dagger Hilt для предоставления зависимостей, связанных с API и сетевыми запросами.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private companion object {
        /**
         * Тип MIME для JSON.
         */
        private val JSON_TYPE = "application/json".toMediaType()

        /**
         * Базовый URL сервера.
         */
        private const val URL_SERVER = BuildConfig.URL_SERVER
    }

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
     * Предоставляет экземпляр [AuthInterceptor] для добавления заголовков аутентификации в запросы.
     *
     * @param userPreferences Хранилище для получения токена аутентификации.
     * @return Экземпляр [AuthInterceptor].
     *
     * @see AuthInterceptor
     */
    @Singleton
    @Provides
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor =
        AuthInterceptor(userPreferences)

    /**
     * Предоставляет экземпляр [OkHttpClient] с настроенными интерцепторами и таймаутами.
     *
     * @param authInterceptor Интерцептор для добавления заголовков аутентификации.
     * @return Экземпляр [OkHttpClient].
     *
     * @see OkHttpClient
     *
     * Фабрика для создания экземпляра OkHttpClient.
     *
     * Этот объект отвечает за настройку и создание экземпляра OkHttpClient, который используется для выполнения сетевых запросов.
     * OkHttpClient настроен с использованием интерсепторов для добавления заголовков и логирования.
     *
     * Экземпляр OkHttpClient, настроенный для работы с API, настроенный для выполнения HTTP-запросов.
     *
     * - Установлено время ожидания соединения в 30 секунд.
     * - В режиме отладки (BuildConfig.DEBUG) добавлен интерсептор [HttpLoggingInterceptor] для логирования тела запросов и ответов.
     * - Добавлен интерсептор для установки заголовков [API_KEY] и [API_AUTHORIZATION] на основе значений из [BuildConfig].
     * - Используется пользовательская DNS-реализация [DnsSelectorHelper].
     *
     * Ленивая инициализация, чтобы создать экземпляр OkHttpClient только при первом обращении.
     * Этот клиент используется для выполнения сетевых запросов к API.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
//            .let { clientOkHttp: OkHttpClient.Builder ->
//                if (BuildConfig.DEBUG) {
//                    clientOkHttp.addInterceptor(
//                        HttpLoggingInterceptor().apply {
//                            level = HttpLoggingInterceptor.Level.BODY
//                        }
//                    )
//                } else {
//                    clientOkHttp
//                }
//            }
            .addInterceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .build()
                )
            }
            .dns(DnsSelectorHelper())
            .build()

    /**
     * Предоставляет экземпляр [Retrofit] для выполнения сетевых запросов.
     *
     * @param okHttpClient Клиент [OkHttpClient], используемый для запросов.
     * @return Экземпляр [Retrofit].
     *
     * @see Retrofit
     *
     * Фабрика для создания экземпляра Retrofit.
     *
     * Этот объект отвечает за настройку и создание экземпляра Retrofit, который используется для выполнения сетевых запросов.
     * Retrofit настроен с использованием OkHttpClient и конвертера для работы с JSON.
     *
     * Ленивая инициализация экземпляра Retrofit.
     * Настраивает базовый URL, конвертер JSON и клиент OkHttp.
     *
     * @return Настроенный экземпляр Retrofit.
     * @see [OkHttpClientFactory]
     *
     * @see Retrofit Основной класс для работы с сетевыми запросами.
     * @see RxJava3CallAdapterFactory Адаптер для интеграции RxJava3 с Retrofit.
     * @see Json Конфигурация JSON-сериализации.
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.URL_SERVER)
            .addConverterFactory(json.asConverterFactory(JSON_TYPE))
            .build()

    /**
     * Предоставляет API для работы с постами.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [PostsApi].
     *
     * @see PostsApi
     */
    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    /**
     * Предоставляет API для работы с событиями.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [EventsApi].
     *
     * @see EventsApi
     */
    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()

    /**
     * Предоставляет API для работы с пользователями.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [UsersApi].
     *
     * @see UsersApi
     */
    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create()

    /**
     * Предоставляет API для работы с вакансиями.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [JobsApi].
     *
     * @see JobsApi
     */
    @Provides
    fun provideJobsApi(retrofit: Retrofit): JobsApi = retrofit.create()

    /**
     * Предоставляет API для работы с медиа.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [MediaApi].
     *
     * @see MediaApi
     */
    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()

    /**
     * Предоставляет API для работы с аутентификацией.
     *
     * @param retrofit Экземпляр [Retrofit].
     * @return Реализация интерфейса [AuthApi].
     *
     * @see AuthApi
     */
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()
}

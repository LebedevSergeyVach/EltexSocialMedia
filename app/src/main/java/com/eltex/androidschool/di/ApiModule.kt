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
import com.eltex.androidschool.utils.DnsSelector

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

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private companion object {
        private val JSON_TYPE = "application/json".toMediaType()
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

    @Singleton
    @Provides
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor =
        AuthInterceptor(userPreferences)

    /**
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
     * - Используется пользовательская DNS-реализация [DnsSelector].
     *
     * Ленивая инициализация, чтобы создать экземпляр OkHttpClient только при первом обращении.
     * Этот клиент используется для выполнения сетевых запросов к API.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
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
                        .build()
                )
            }
            .dns(DnsSelector())
            .build()

    /**
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

    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()

    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create()

    @Provides
    fun provideJobsApi(retrofit: Retrofit): JobsApi = retrofit.create()

    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create()

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create()
}

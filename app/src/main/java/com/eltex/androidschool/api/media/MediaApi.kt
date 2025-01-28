package com.eltex.androidschool.api.media

import com.eltex.androidschool.api.common.RetrofitFactory

import okhttp3.MultipartBody

import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Интерфейс для работы с API медиа-файлов.
 * Этот интерфейс предоставляет методы для загрузки медиа-файлов на сервер.
 */
interface MediaApi {

    /**
     * Загружает медиа-файл на сервер.
     *
     * @param file Медиа-файл, который нужно загрузить, представленный как часть MultipartBody.
     * @return Возвращает объект [MediaDto], содержащий URL загруженного файла.
     * @see MediaDto
     *
     * @throws IOException Если произошла ошибка при загрузке файла.
     */
    @Multipart
    @POST("api/media")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): MediaDto

    /**
     * Объект-компаньон для создания экземпляра [MediaApi].
     * Использует ленивую инициализацию для создания экземпляра Retrofit и его настройки.
     */
    companion object {
        /**
         * Экземпляр [MediaApi], созданный с использованием [RetrofitFactory].
         * Этот экземпляр используется для выполнения запросов к API медиа-файлов.
         *
         * @see RetrofitFactory
         */
        val INSTANCE: MediaApi by lazy {
            RetrofitFactory.INSTANCE.create<MediaApi>()
        }
    }
}

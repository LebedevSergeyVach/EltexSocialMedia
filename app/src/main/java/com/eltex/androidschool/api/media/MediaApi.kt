package com.eltex.androidschool.api.media

import com.eltex.androidschool.data.media.MediaDto

import okhttp3.MultipartBody

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
}

package com.eltex.androidschool.repository.events

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.utils.Callback
import com.eltex.androidschool.utils.DnsSelector

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import okhttp3.logging.HttpLoggingInterceptor

import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Репозиторий для работы с данными событий через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления, удаления и управления событиями.
 *
 * @property EventRepository
 */
class NetworkEventRepository : EventRepository {

    private companion object {
        val JSON_TYPE = "application/json".toMediaType()
        private const val API_KEY = "Api-Key"
        private const val API_AUTHORIZATION = "Authorization"
        private const val API_URL_EVENTS = "https://eltex-android.ru/api/events"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Экземпляр клиента OkHttp, настроенный для выполнения HTTP-запросов.
     *
     * - Установлено время ожидания соединения в 30 секунд.
     * - В режиме отладки (BuildConfig.DEBUG) добавлен интерсептор [HttpLoggingInterceptor] для логирования тела запросов и ответов.
     * - Добавлен интерсептор для установки заголовков [API_KEY] и [API_AUTHORIZATION] на основе значений из [BuildConfig].
     * - Используется пользовательская DNS-реализация [DnsSelector].
     *
     * Этот клиент используется для выполнения сетевых запросов к API.
     */
    private val client = OkHttpClient.Builder()
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

    /**
     * Получает список всех событий с сервера.
     *
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun getEvents(callback: Callback<List<EventData>>) {
        val call: Call = client.newCall(
            Request.Builder()
                .url(API_URL_EVENTS)
                .build()
        )

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                data = json.decodeFromString(requireNotNull(response.body).string())
                            )
                        } catch (e: Exception) {
                            callback.onError(exception = e)
                        }
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }
            }
        )
    }

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun likeById(eventId: Long, likedByMe: Boolean, callback: Callback<EventData>) {
        val call: Call = if (likedByMe) {
            client.newCall(
                Request.Builder()
                    .url("$API_URL_EVENTS/$eventId/likes")
                    .delete()
                    .build()
            )
        } else {
            client.newCall(
                Request.Builder()
                    .url("$API_URL_EVENTS/$eventId/likes")
                    .post(EMPTY_REQUEST)
                    .build()
            )
        }

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                data = json.decodeFromString(requireNotNull(response.body).string())
                            )
                        } catch (e: Exception) {
                            callback.onError(exception = e)
                        }
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }

            }
        )
    }

    /**
     * Участвует или отказывается от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun participateById(
        eventId: Long,
        participatedByMe: Boolean,
        callback: Callback<EventData>
    ) {
        val call: Call = if (participatedByMe) {
            client.newCall(
                Request.Builder()
                    .url("$API_URL_EVENTS/$eventId/participants")
                    .delete()
                    .build()
            )
        } else {
            client.newCall(
                Request.Builder()
                    .url("$API_URL_EVENTS/$eventId/participants")
                    .post(EMPTY_REQUEST)
                    .build()
            )
        }

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                data = json.decodeFromString(requireNotNull(response.body).string())
                            )
                        } catch (e: Exception) {
                            callback.onError(exception = e)
                        }
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }

            }
        )
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun deleteById(eventId: Long, callback: Callback<Unit>) {
        val call: Call = client.newCall(
            Request.Builder()
                .url("$API_URL_EVENTS/$eventId")
                .delete()
                .build()
        )

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }
            }
        )
    }

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события.
     * @param content Содержимое события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String,
        callback: Callback<EventData>
    ) {
        val call: Call = client.newCall(
            Request.Builder()
                .url(API_URL_EVENTS)
                .post(
                    json.encodeToString(
                        EventData(
                            id = eventId,
                            content = content,
                            link = link,
                            optionConducting = option,
                            dataEvent = data
                        )
                    ).toRequestBody(JSON_TYPE)
                )
                .build()
        )

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                json.decodeFromString(requireNotNull(response.body).string())
                            )
                        } catch (e: Exception) {
                            callback.onError(exception = e)
                        }
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }
            }
        )
    }

    /**
     * Получает событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun getEventById(eventId: Long, callback: Callback<EventData>) {
        val call: Call = client.newCall(
            Request.Builder()
                .url("$API_URL_EVENTS/$eventId")
                .build()
        )

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(exception = e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                json.decodeFromString(requireNotNull(response.body).string())
                            )
                        } catch (e: Exception) {
                            callback.onError(exception = e)
                        }
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
                        )
                    }
                }
            }
        )
    }
}

//package com.eltex.androidschool.development.repository
//
//import com.eltex.androidschool.BuildConfig
//import com.eltex.androidschool.data.posts.PostData
//import com.eltex.androidschool.utils.Callback
//import com.eltex.androidschool.utils.DnsSelector
//
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//
//import okhttp3.Call
//import okhttp3.Interceptor
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.Response
//import okhttp3.internal.EMPTY_REQUEST
//import okhttp3.logging.HttpLoggingInterceptor
//
//import java.io.IOException
//import java.lang.Exception
//import java.util.concurrent.TimeUnit
//
///**
// * Репозиторий для работы с данными постов через сетевой API.
// *
// * Этот класс отвечает за взаимодействие с сервером для получения, обновления и удаления постов.
// *
// * @property PostRepository
// */
//class NetworkPostRepository : PostRepository {
//
//    private companion object {
//        private val JSON_TYPE = "application/json".toMediaType()
//        private const val API_KEY = "Api-Key"
//        private const val API_AUTHORIZATION = "Authorization"
//        private const val API_URL_POST = "https://eltex-android.ru/api/posts"
//    }
//
//    private val json = Json {
//        ignoreUnknownKeys = true
//    }
//
//    /**
//     * Экземпляр клиента OkHttp, настроенный для выполнения HTTP-запросов.
//     *
//     * - Установлено время ожидания соединения в 30 секунд.
//     * - В режиме отладки (BuildConfig.DEBUG) добавлен интерсептор [HttpLoggingInterceptor] для логирования тела запросов и ответов.
//     * - Добавлен интерсептор для установки заголовков [API_KEY] и [API_AUTHORIZATION] на основе значений из [BuildConfig].
//     * - Используется пользовательская DNS-реализация [DnsSelector].
//     *
//     * Этот клиент используется для выполнения сетевых запросов к API.
//     */
//    private val client = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .let { clientOkHttp: OkHttpClient.Builder ->
//            if (BuildConfig.DEBUG) {
//                clientOkHttp.addInterceptor(
//                    HttpLoggingInterceptor().apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    }
//                )
//            } else {
//                clientOkHttp
//            }
//        }
//        .addInterceptor { chain: Interceptor.Chain ->
//            chain.proceed(
//                chain.request().newBuilder()
//                    .addHeader(API_KEY, BuildConfig.API_KEY)
//                    .addHeader(API_AUTHORIZATION, BuildConfig.AUTHORIZATION)
//                    .build()
//            )
//        }
//        .dns(DnsSelector())
//        .build()
//
//    /**
//     * Получает список всех постов с сервера.
//     *
//     * @param callback Обратный вызов для обработки результата запроса.
//     */
//    override fun getPosts(callback: Callback<List<PostData>>) {
//        val call: Call = client.newCall(
//            Request.Builder()
//                .url(API_URL_POST)
//                .build()
//        )
//
//        call.enqueue(
//            object : okhttp3.Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(exception = e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        try {
//                            callback.onSuccess(
//                                data = json.decodeFromString(requireNotNull(response.body).string())
//                            )
//                        } catch (e: Exception) {
//                            callback.onError(exception = e)
//                        }
//                    } else {
//                        callback.onError(
//                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
//                        )
//                    }
//                }
//            }
//        )
//    }
//
//    /**
//     * Поставить или убрать лайк у поста по его идентификатору.
//     *
//     * @param postId Идентификатор поста.
//     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
//     * @param callback Обратный вызов для обработки результата запроса.
//     */
//    override fun likeById(postId: Long, likedByMe: Boolean, callback: Callback<PostData>) {
//        val call: Call = if (likedByMe) {
//            client.newCall(
//                Request.Builder()
//                    .url("$API_URL_POST/$postId/likes")
//                    .delete()
//                    .build()
//            )
//        } else {
//            client.newCall(
//                Request.Builder()
//                    .url("$API_URL_POST/$postId/likes")
//                    .post(EMPTY_REQUEST)
//                    .build()
//            )
//        }
//
//        call.enqueue(
//            object : okhttp3.Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(exception = e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        try {
//                            callback.onSuccess(
//                                json.decodeFromString(requireNotNull(response.body).string())
//                            )
//                        } catch (e: Exception) {
//                            callback.onError(exception = e)
//                        }
//                    } else {
//                        callback.onError(
//                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
//                        )
//                    }
//                }
//            }
//        )
//    }
//
//    /**
//     * Удаляет пост по его идентификатору.
//     *
//     * @param postId Идентификатор поста.
//     * @param callback Обратный вызов для обработки результата запроса.
//     */
//    override fun deleteById(postId: Long, callback: Callback<Unit>) {
//        val call: Call = client.newCall(
//            Request.Builder()
//                .url("$API_URL_POST/$postId")
//                .delete()
//                .build()
//        )
//
//        call.enqueue(
//            object : okhttp3.Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(exception = e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        callback.onSuccess(Unit)
//                    } else {
//                        callback.onError(
//                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
//                        )
//                    }
//                }
//            }
//        )
//    }
//
//    /**
//     * Обновляет пост по его идентификатору или добавляет новый пост.
//     *
//     * @param postId Идентификатор поста.
//     * @param content Новое содержимое поста.
//     * @param callback Обратный вызов для обработки результата запроса.
//     */
//    override fun save(postId: Long, content: String, callback: Callback<PostData>) {
//        val call: Call = client.newCall(
//            Request.Builder()
//                .url(API_URL_POST)
//                .post(
//                    json.encodeToString(
//                        PostData(
//                            id = postId,
//                            content = content
//                        )
//                    ).toRequestBody(JSON_TYPE)
//                )
//                .build()
//        )
//
//        call.enqueue(
//            object : okhttp3.Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(exception = e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        try {
//                            callback.onSuccess(
//                                json.decodeFromString(requireNotNull(response.body).string())
//                            )
//                        } catch (e: Exception) {
//                            callback.onError(exception = e)
//                        }
//                    } else {
//                        callback.onError(
//                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
//                        )
//                    }
//                }
//            }
//        )
//    }
//
//    /**
//     * Получает пост по его идентификатору.
//     *
//     * @param postId Идентификатор поста.
//     * @param callback Обратный вызов для обработки результата запроса.
//     */
//    fun getPostById(postId: Long, callback: Callback<PostData>) {
//        val call: Call = client.newCall(
//            Request.Builder()
//                .url("$API_URL_POST/$postId")
//                .build()
//        )
//
//        call.enqueue(
//            object : okhttp3.Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(exception = e)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.isSuccessful) {
//                        try {
//                            callback.onSuccess(
//                                json.decodeFromString(requireNotNull(response.body).string())
//                            )
//                        } catch (e: Exception) {
//                            callback.onError(exception = e)
//                        }
//                    } else {
//                        callback.onError(
//                            RuntimeException("Response code is/Код ответа с сервера ${response.code}")
//                        )
//                    }
//                }
//            }
//        )
//    }
//}

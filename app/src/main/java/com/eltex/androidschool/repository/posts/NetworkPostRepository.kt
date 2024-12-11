package com.eltex.androidschool.repository.posts

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback as RetrofitCallback

import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.utils.Callback as DomainCallback

/**
 * Репозиторий для работы с данными постов через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления и удаления постов.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @property PostRepository
 * @property PostsApi
 */
class NetworkPostRepository : PostRepository {
    /**
     * Получает список всех постов с сервера.
     *
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun getPosts(callback: DomainCallback<List<PostData>>) {
        val call = PostsApi.INSTANCE.getAllPosts()

        call.enqueue(
            object : RetrofitCallback<List<PostData>> {
                override fun onResponse(
                    call: Call<List<PostData>>, response: Response<List<PostData>>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<List<PostData>>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun likeById(postId: Long, likedByMe: Boolean, callback: DomainCallback<PostData>) {
        val call = if (likedByMe) {
            PostsApi.INSTANCE.unlikePostById(postId)
        } else {
            PostsApi.INSTANCE.likePostById(postId)
        }

        call.enqueue(
            object : RetrofitCallback<PostData> {
                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<PostData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun deleteById(postId: Long, callback: DomainCallback<Unit>) {
        val call = PostsApi.INSTANCE.deletePostById(postId)

        call.enqueue(
            object : RetrofitCallback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<Unit>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Обновляет пост по его идентификатору или добавляет новый пост.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    override fun save(postId: Long, content: String, callback: DomainCallback<PostData>) {
        val call = PostsApi.INSTANCE.savePost(
            PostData(
                id = postId,
                content = content
            )
        )

        call.enqueue(
            object : RetrofitCallback<PostData> {
                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<PostData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun getPostById(postId: Long, callback: DomainCallback<PostData>) {
        val call = PostsApi.INSTANCE.getPostById(postId)

        call.enqueue(
            object : RetrofitCallback<PostData> {
                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(requireNotNull(response.body()))
                    } else {
                        callback.onError(
                            RuntimeException("Response code is/Код ответа с сервера ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<PostData>, throwable: Throwable) {
                    callback.onError(exception = throwable)
                }
            }
        )
    }
}

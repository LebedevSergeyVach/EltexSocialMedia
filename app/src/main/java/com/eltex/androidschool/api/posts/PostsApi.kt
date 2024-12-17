package com.eltex.androidschool.api.posts

import com.eltex.androidschool.data.posts.PostData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Интерфейс для работы с API постов.
 *
 * Этот интерфейс определяет методы для выполнения сетевых запросов к API постов.
 * Каждый метод соответствует определённому HTTP-методу и URL.
 */
interface PostsApi {
    /**
     * Получает список всех постов.
     *
     * @return Call<List<PostData>> Объект Call для выполнения запроса.
     */
    @GET("api/posts")
    fun getAllPosts(): Single<List<PostData>>

    /**
     * Сохраняет или обновляет пост.
     *
     * @param post Объект PostData, который нужно сохранить или обновить.
     * @return Call<PostData> Объект Call для выполнения запроса.
     */
    @POST("api/posts")
    fun savePost(@Body post: PostData): Single<PostData>

    /**
     * Поставить лайк посту по его идентификатору.
     *
     * @param postId Идентификатор поста, которому нужно поставить лайк.
     * @return Call<PostData> Объект Call для выполнения запроса.
     */
    @POST("api/posts/{id}/likes")
    fun likePostById(@Path("id") postId: Long): Single<PostData>

    /**
     * Убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, у которого нужно убрать лайк.
     * @return Call<PostData> Объект Call для выполнения запроса.
     */
    @DELETE("api/posts/{id}/likes")
    fun unlikePostById(@Path("id") postId: Long): Single<PostData>

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     * @return Call<Unit> Объект Call для выполнения запроса.
     */
    @DELETE("api/posts/{id}")
    fun deletePostById(@Path("id") postId: Long): Completable

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно получить.
     * @return Call<PostData> Объект Call для выполнения запроса.
     */
    @GET("api/posts/{id}")
    fun getPostById(@Path("id") postId: Long): Single<PostData>

    /**
     * Объект-компаньон для создания экземпляра PostsApi.
     *
     * Использует ленивую инициализацию для создания экземпляра Retrofit и его настройки.
     */
    companion object {
        /**
         * Экземпляр PostsApi, созданный с использованием RetrofitFactoryPost.
         */
        val INSTANCE by lazy {
            RetrofitFactoryPost.INSTANCE.create<PostsApi>()
        }
    }
}

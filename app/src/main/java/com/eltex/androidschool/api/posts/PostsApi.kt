package com.eltex.androidschool.api.posts

import com.eltex.androidschool.api.common.RetrofitFactory
import com.eltex.androidschool.data.posts.PostData

import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Интерфейс для работы с API постов.
 * Этот интерфейс определяет методы для выполнения сетевых запросов к API постов.
 * Каждый метод соответствует определённому HTTP-методу и URL.
 *
 * @see PostData Класс, представляющий данные поста.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface PostsApi {

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора.
     *
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     *
     * @return List<[PostData]> Список постов.
     */
    @GET("api/posts/{id}/before")
    suspend fun getBeforePosts(@Path("id") id: Long, @Query("count") count: Int): List<PostData>

    /**
     * Получает последние посты.
     *
     * @param count Количество постов, которые нужно загрузить.
     *
     * @return List<[PostData]> Список последних постов.
     */
    @GET("api/posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int): List<PostData>

    /**
     * Получает список всех постов.
     *
     * @return List<[PostData]> Список всех постов.
     */
    @GET("api/posts")
    suspend fun getAllPosts(): List<PostData>

    /**
     * Сохраняет или обновляет пост.
     *
     * @param post Объект PostData, который нужно сохранить или обновить.
     * @return PostData Обновленный или сохраненный пост.
     */
    @POST("api/posts")
    suspend fun savePost(@Body post: PostData): PostData

    /**
     * Поставить лайк посту по его идентификатору.
     *
     * @param postId Идентификатор поста, которому нужно поставить лайк.
     * @return PostData Пост с обновленным состоянием лайка.
     */
    @POST("api/posts/{id}/likes")
    suspend fun likePostById(@Path("id") postId: Long): PostData

    /**
     * Убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, у которого нужно убрать лайк.
     * @return PostData Пост с обновленным состоянием лайка.
     */
    @DELETE("api/posts/{id}/likes")
    suspend fun unlikePostById(@Path("id") postId: Long): PostData

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    @DELETE("api/posts/{id}")
    suspend fun deletePostById(@Path("id") postId: Long)

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно получить.
     * @return PostData Пост, соответствующий указанному идентификатору.
     */
    @GET("api/posts/{id}")
    suspend fun getPostById(@Path("id") postId: Long): PostData

    /**
     * Получает посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     *
     * @return [List]<[PostData]> Список постов пользователя.
     */
    @GET("api/{authorId}/wall")
    suspend fun getAllPostsByAuthorId(@Path("authorId") authorId: Long): List<PostData>

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список постов.
     */
    @GET("api/{authorId}/wall/{id}/before")
    suspend fun getBeforePostsByAuthorId(
        @Path("authorId") authorId: Long,
        @Path("id") id: Long,
        @Query("count") count: Int
    ): List<PostData>

    /**
     * Получает последние посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param count Количество постов, которые нужно загрузить.
     *
     * @return [List]<[PostData]> Список последних постов.
     */
    @GET("api/{authorId}/wall/latest")
    suspend fun getLatestPostsByAuthorId(
        @Path("authorId") authorId: Long,
        @Query("count") count: Int
    ): List<PostData>

    /**
     * Объект-компаньон для создания экземпляра [PostsApi].
     * Использует ленивую инициализацию для создания экземпляра Retrofit и его настройки.
     */
    companion object {
        /**
         * Экземпляр [PostsApi], созданный с использованием [RetrofitFactory].
         */
        val INSTANCE: PostsApi by lazy {
            RetrofitFactory.INSTANCE.create<PostsApi>()
        }
    }
}

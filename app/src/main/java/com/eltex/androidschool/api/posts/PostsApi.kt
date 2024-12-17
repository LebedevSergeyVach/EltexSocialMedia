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
 *
 * @see PostData Класс, представляющий данные поста.
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
interface PostsApi {
    /**
     * Получает список всех постов.
     *
     * @return Single<List<PostData>> Объект Single для выполнения запроса.
     */
    @GET("api/posts")
    fun getAllPosts(): Single<List<PostData>>

    /**
     * Сохраняет или обновляет пост.
     *
     * @param post Объект PostData, который нужно сохранить или обновить.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    @POST("api/posts")
    fun savePost(@Body post: PostData): Single<PostData>

    /**
     * Поставить лайк посту по его идентификатору.
     *
     * @param postId Идентификатор поста, которому нужно поставить лайк.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    @POST("api/posts/{id}/likes")
    fun likePostById(@Path("id") postId: Long): Single<PostData>

    /**
     * Убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, у которого нужно убрать лайк.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    @DELETE("api/posts/{id}/likes")
    fun unlikePostById(@Path("id") postId: Long): Single<PostData>

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     * @return Completable Объект Completable для выполнения запроса.
     */
    @DELETE("api/posts/{id}")
    fun deletePostById(@Path("id") postId: Long): Completable

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно получить.
     * @return Single<PostData> Объект Single для выполнения запроса.
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

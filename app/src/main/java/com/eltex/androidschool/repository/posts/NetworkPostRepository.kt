package com.eltex.androidschool.repository.posts

import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.data.posts.PostData

import io.reactivex.rxjava3.core.Single

/**
 * Репозиторий для работы с данными постов через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления и удаления постов.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @see PostRepository Интерфейс репозитория, который реализует этот класс.
 * @see PostsApi Интерфейс для работы с API постов.
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
class NetworkPostRepository : PostRepository {
    /**
     * Получает список всех постов с сервера.
     *
     * @return Single<List<PostData>> Объект Single для выполнения запроса.
     */
    override fun getPosts() =
        PostsApi.INSTANCE.getAllPosts()

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    override fun likeById(postId: Long, likedByMe: Boolean): Single<PostData> {
        return if (likedByMe) {
            PostsApi.INSTANCE.unlikePostById(postId = postId)
        } else {
            PostsApi.INSTANCE.likePostById(postId = postId)
        }
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @return Completable Объект Completable для выполнения запроса.
     */
    override fun deleteById(postId: Long) =
        PostsApi.INSTANCE.deletePostById(postId = postId)

    /**
     * Обновляет пост по его идентификатору или добавляет новый пост.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    override fun save(postId: Long, content: String) =
        PostsApi.INSTANCE.savePost(
            post = PostData(
                id = postId,
                content = content,
            )
        )

    /**
     * Получает пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    fun getPostById(postId: Long) =
        PostsApi.INSTANCE.getPostById(postId = postId)
}

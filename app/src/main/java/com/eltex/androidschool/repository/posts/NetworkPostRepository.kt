package com.eltex.androidschool.repository.posts

import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.data.posts.PostData

/**
 * Репозиторий для работы с данными постов через сетевой API.
 *
 * Этот класс отвечает за взаимодействие с сервером для получения, обновления и удаления постов.
 * Он использует Retrofit для выполнения сетевых запросов и обработки ответов.
 *
 * @see PostRepository Интерфейс репозитория, который реализует этот класс.
 * @see PostsApi Интерфейс для работы с API постов.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
class NetworkPostRepository : PostRepository {
    /**
     * Получает список всех постов с сервера.
     *
     * @return List<[PostData]> Список постов, полученных с сервера.
     */
    override suspend fun getPosts() =
        PostsApi.INSTANCE.getAllPosts()

    /**
     * Поставить или убрать лайк у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return [PostData] Пост с обновленным состоянием лайка.
     */
    override suspend fun likeById(postId: Long, likedByMe: Boolean): PostData {
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
     */
    override suspend fun deleteById(postId: Long) =
        PostsApi.INSTANCE.deletePostById(postId = postId)

    /**
     * Сохраняет или обновляет пост.
     * Если идентификатор события равен 0, то создается новый пост.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержимое поста.
     * @return [PostData] Обновленный или сохраненный пост.
     */
    override suspend fun save(postId: Long, content: String) =
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
     * @return [PostData] Пост, соответствующий указанному идентификатору.
     */
    suspend fun getPostById(postId: Long) =
        PostsApi.INSTANCE.getPostById(postId = postId)

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя, который нужно получить.
     * @return [PostData] Пользователь, соответствующий указанному идентификатору.
     */
    suspend fun getUserById(userId: Long) =
        PostsApi.INSTANCE.getUserById(userId = userId)
}

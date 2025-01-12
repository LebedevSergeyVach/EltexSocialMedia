package com.eltex.androidschool.repository.posts

import com.eltex.androidschool.data.posts.PostData

/**
 * Интерфейс репозитория для работы с постами.
 * Предоставляет методы для получения списка постов, лайков постов и управления ими.
 *
 * @see NetworkPostRepository Реализация интерфейса в памяти.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface PostRepository {

    /**
     * Возвращает список постов.
     *
     * @return List<[PostData]> Список постов, полученных с сервера.
     */
    suspend fun getPosts(): List<PostData>

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return [PostData] Пост с обновленным состоянием лайка.
     */
    suspend fun likeById(postId: Long, likedByMe: Boolean): PostData

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    suspend fun deleteById(postId: Long)

    /**
     * Обновляет пост по его идентификатору или добавляет новый пост.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     * @return [PostData] Обновленный или сохраненный пост.
     */
    suspend fun save(postId: Long, content: String): PostData

    /**
     * Получает посты определенного польщователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя, который нужно получить.
     * @return List<[PostData]> Посты, соответствующий указанному идентификатору пользователя.
     */
    suspend fun getPostsByAuthorId(authorId: Long): List<PostData>
}

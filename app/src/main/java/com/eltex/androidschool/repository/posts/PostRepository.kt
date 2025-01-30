package com.eltex.androidschool.repository.posts

import android.content.Context

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.viewmodel.common.FileModel

/**
 * Интерфейс репозитория для работы с постами.
 * Этот интерфейс предоставляет методы для получения, обновления и удаления постов.
 *
 * @see NetworkPostRepository Реализация интерфейса для работы с сетевыми данными.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface PostRepository {

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора.
     *
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список постов.
     */
    suspend fun getBeforePosts(id: Long, count: Int): List<PostData>

    /**
     * Получает последние посты.
     *
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список последних постов.
     */
    suspend fun getLatestPosts(count: Int): List<PostData>

    /**
     * Получает список всех постов.
     *
     * @return List<PostData> Список всех постов.
     */
    suspend fun getPosts(): List<PostData>

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return PostData Пост с обновленным состоянием лайка.
     */
    suspend fun likeById(postId: Long, likedByMe: Boolean): PostData

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста.
     */
    suspend fun deleteById(postId: Long)

    /**
     * Сохраняет или обновляет пост.
     *
     * @param postId Идентификатор поста.
     * @param content Новое содержание поста.
     * @return PostData Обновленный или сохраненный пост.
     */
    suspend fun save(
        postId: Long,
        content: String,
        fileModel: FileModel?,
        context: Context,
        onProgress: (Int) -> Unit,
    ): PostData

    /**
     * Получает посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @return List<PostData> Список постов пользователя.
     */
    suspend fun getPostsByAuthorId(authorId: Long): List<PostData>

    /**
     * Получает список постов, которые были опубликованы до указанного идентификатора определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param id Идентификатор поста, начиная с которого нужно загрузить предыдущие посты.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список постов.
     */
    suspend fun getBeforePostsByAuthorId(authorId: Long, id: Long, count: Int): List<PostData>

    /**
     * Получает последние посты определенного пользователя по его идентификатору.
     *
     * @param authorId Идентификатор пользователя.
     * @param count Количество постов, которые нужно загрузить.
     * @return List<PostData> Список последних постов.
     */
    suspend fun getLatestPostsByAuthorId(authorId: Long, count: Int): List<PostData>
}

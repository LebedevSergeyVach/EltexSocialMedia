package com.eltex.androidschool.repository.posts

import com.eltex.androidschool.data.posts.PostData

import com.eltex.androidschool.utils.Callback

/**
 * Интерфейс репозитория для работы с постами.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see NetworkPostRepository Реализация интерфейса в памяти.
 */
interface PostRepository {
    /**
     * Возвращает Flow, который излучает список постов.
     *
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun getPosts(callback: Callback<List<PostData>>)

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun likeById(postId: Long, likedByMe: Boolean, callback: Callback<PostData>)

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun deleteById(postId: Long, callback: Callback<Unit>)

    /**
     * Обновляет пост по его идентификатору.
     * Добавляет новый пост.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun save(postId: Long, content: String, callback: Callback<PostData>)
}

package com.eltex.androidschool.repository

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository

/**
 * Интерфейс для мокирования репозитория постов в тестах.
 *
 * Этот интерфейс расширяет `PostRepository` и предоставляет методы для мокирования поведения репозитория.
 * Каждый метод выбрасывает исключение `Not mocked`, чтобы указать, что его нужно переопределить в тестах.
 *
 * @see PostRepository Интерфейс репозитория, который мокируется.
 */
interface TestPostRepository : PostRepository {
    /**
     * Возвращает список постов.
     *
     * @return List<PostData> Список постов, полученных с сервера.
     */
    override suspend fun getPosts(): List<PostData> = error("Not mocked")

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return PostData Пост с обновленным состоянием лайка.
     */
    override suspend fun likeById(postId: Long, likedByMe: Boolean): PostData = error("Not mocked")

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    override suspend fun deleteById(postId: Long): Unit = error("Not mocked")

    /**
     * Обновляет пост по его идентификатору или добавляет новый пост.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     * @return PostData Обновленный или сохраненный пост.
     */
    override suspend fun save(postId: Long, content: String): PostData = error("Not mocked")
}

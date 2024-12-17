package com.eltex.androidschool.repository.posts

import com.eltex.androidschool.data.posts.PostData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Интерфейс репозитория для работы с постами.
 * Предоставляет методы для получения списка постов и лайков постов.
 *
 * @see NetworkPostRepository Реализация интерфейса в памяти.
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
interface PostRepository {
    /**
     * Возвращает Single, который излучает список постов.
     *
     * @return Single<List<PostData>> Объект Single для выполнения запроса.
     */
    fun getPosts(): Single<List<PostData>> = Single.never()

    /**
     * Переключает состояние лайка у поста по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    fun likeById(postId: Long, likedByMe: Boolean): Single<PostData> = Single.never()

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     * @return Completable Объект Completable для выполнения запроса.
     */
    fun deleteById(postId: Long): Completable = Completable.complete()

    /**
     * Обновляет пост по его идентификатору.
     * Добавляет новый пост.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание поста.
     * @return Single<PostData> Объект Single для выполнения запроса.
     */
    fun save(postId: Long, content: String): Single<PostData> = Single.never()
}

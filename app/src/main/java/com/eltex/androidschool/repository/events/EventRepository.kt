package com.eltex.androidschool.repository.events

import com.eltex.androidschool.data.events.EventData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Интерфейс репозитория для работы с событиями.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see NetworkEventRepository Реализация интерфейса для работы с сетевым API.
 * @see Single Класс из RxJava3, используемый для асинхронных операций, возвращающих одно значение.
 * @see Completable Класс из RxJava3, используемый для асинхронных операций, не возвращающих значения.
 */
interface EventRepository {
    /**
     * Возвращает список событий.
     *
     * @return Single<List<EventData>> Объект Single для выполнения запроса.
     */
    fun getEvents(): Single<List<EventData>> = Single.never()

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    fun likeById(eventId: Long, likedByMe: Boolean): Single<EventData> = Single.never()

    /**
     * Переключает состояние участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    fun participateById(eventId: Long, participatedByMe: Boolean): Single<EventData> =
        Single.never()

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     * @return Completable Объект Completable для выполнения запроса.
     */
    fun deleteById(eventId: Long): Completable = Completable.complete()

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события, которое нужно обновить.
     * @param content Содержание события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @return Single<EventData> Объект Single для выполнения запроса.
     */
    fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ): Single<EventData> = Single.never()
}

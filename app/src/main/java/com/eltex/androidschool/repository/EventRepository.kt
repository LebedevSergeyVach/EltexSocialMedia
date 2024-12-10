package com.eltex.androidschool.repository

import com.eltex.androidschool.data.EventData

import com.eltex.androidschool.utils.Callback

/**
 * Интерфейс репозитория для работы с событиями.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see NetworkEventRepository Реализация интерфейса в памяти.
 */
interface EventRepository {

    /**
     * Возвращает список событий.
     *
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun getEvents(callback: Callback<List<EventData>>)

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun likeById(eventId: Long, likedByMe: Boolean, callback: Callback<EventData>)

    /**
     * Переключает состояние участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun participateById(eventId: Long, participatedByMe: Boolean, callback: Callback<EventData>)

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun deleteById(eventId: Long, callback: Callback<Unit>)

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события, которое нужно обновить.
     * @param content Содержание события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @param callback Обратный вызов для обработки результата запроса.
     */
    fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String,
        callback: Callback<EventData>
    )
}

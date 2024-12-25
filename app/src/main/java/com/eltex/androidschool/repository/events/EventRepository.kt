package com.eltex.androidschool.repository.events

import com.eltex.androidschool.data.events.EventData

/**
 * Интерфейс репозитория для работы с событиями.
 * Предоставляет методы для получения списка событий, лайков, участия в событиях и управления ими.
 *
 * @see NetworkEventRepository Реализация интерфейса для работы с сетевым API.
 * @see suspend Функции, которые могут быть приостановлены и возобновлены позже.
 */
interface EventRepository {
    /**
     * Возвращает список событий.
     *
     * @return List<[EventData]> Список событий, полученных с сервера.
     */
    suspend fun getEvents(): List<EventData>

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return [EventData] Событие с обновленным состоянием лайка.
     */
    suspend fun likeById(eventId: Long, likedByMe: Boolean): EventData

    /**
     * Переключает состояние участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @return [EventData] Событие с обновленным состоянием участия.
     */
    suspend fun participateById(eventId: Long, participatedByMe: Boolean): EventData

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     */
    suspend fun deleteById(eventId: Long)

    /**
     * Сохраняет или обновляет событие.
     * Если идентификатор события равен 0, то создается новое событие.
     *
     * @param eventId Идентификатор события, которое нужно обновить.
     * @param content Содержание события.
     * @param link Ссылка на событие.
     * @param option Дополнительная опция.
     * @param data Дата события.
     * @return [EventData] Обновленное или сохраненное событие.
     */
    suspend fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ): EventData
}

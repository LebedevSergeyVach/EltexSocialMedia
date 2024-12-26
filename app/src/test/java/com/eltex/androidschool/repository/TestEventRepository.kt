package com.eltex.androidschool.repository

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository

/**
 * Интерфейс для мокирования репозитория событий в тестах.
 *
 * Этот интерфейс расширяет [EventRepository] и предоставляет методы для мокирования поведения репозитория.
 * Каждый метод выбрасывает исключение `Not mocked`, чтобы указать, что его нужно переопределить в тестах.
 *
 * @see EventRepository Интерфейс репозитория, который мокируется.
 */
interface TestEventRepository : EventRepository {

    /**
     * Возвращает список событий.
     *
     * @return List<[EventData]> Список событий, полученных с сервера.
     */
    override suspend fun getEvents(): List<EventData> = error("Not mocked")

    /**
     * Переключает состояние лайка у события по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return [EventData] Событие с обновленным состоянием лайка.
     */
    override suspend fun likeById(eventId: Long, likedByMe: Boolean): EventData =
        error("Not mocked")

    /**
     * Переключает состояние участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @return [EventData] Событие с обновленным состоянием участия.
     */
    override suspend fun participateById(eventId: Long, participatedByMe: Boolean): EventData =
        error("Not mocked")

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     */
    override suspend fun deleteById(eventId: Long): Unit = error("Not mocked")

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
    override suspend fun save(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    ): EventData = error("Not mocked")
}

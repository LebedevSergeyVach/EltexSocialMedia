package com.eltex.androidschool.dao

import com.eltex.androidschool.data.EventData

/**
 * Интерфейс для работы с данными событий в базе данных.
 *
 * @see EventData Класс, представляющий данные события.
 */
interface EventDao {
    /**
     * Получает все события из базы данных.
     *
     * @return Список всех событий.
     */
    fun getAll(): List<EventData>

    /**
     * Сохраняет новое событие в базу данных.
     *
     * @param event Событие для сохранения.
     * @return Сохраненное событие с обновленным идентификатором.
     */
    fun save(event: EventData): EventData

    /**
     * Поставить или убрать лайк у события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Обновленное событие.
     */
    fun likeById(eventId: Long): EventData

    /**
     * Участвовать или отказаться от участия в событии по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @return Обновленное событие.
     */
    fun participateById(eventId: Long): EventData

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события.
     */
    fun deleteById(eventId: Long)

    /**
     * Обновляет содержимое события по его идентификатору.
     *
     * @param eventId Идентификатор события.
     * @param content Новое содержимое события.
     * @param link Новый URL-адрес события.
     * @param option Новая опция проведения события.
     * @param data Новая дата события.
     */
    fun updateById(
        eventId: Long,
        content: String,
        link: String,
        option: String,
        data: String
    )
}

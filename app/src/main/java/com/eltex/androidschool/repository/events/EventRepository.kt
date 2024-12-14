package com.eltex.androidschool.repository.events

import com.eltex.androidschool.data.events.EventData

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с событиями.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see DaoSQLiteEventRepository Реализация интерфейса в памяти.
 */
interface EventRepository {

    /**
     * Возвращает Flow, который излучает список событий.
     *
     */
    fun getEvent(): Flow<List<EventData>>

    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     */
    fun likeById(eventId: Long)

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     */
    fun participateById(eventId: Long)

    /**
     * Удаления события по его id.
     *
     * @param eventId Идентификатор события, который нужно удалить.
     */
    fun deleteById(eventId: Long)

    /**
     * Обновляет событие по его id.
     *
     * @param eventId Идентификатор события, который нужно обновить.
     * @param content Новое содержание события.
     * @param link Новая ссылка события.
     */
    fun updateById(eventId: Long, content: String, link: String, option: String, data: String)

    /**
     * Добавляет новое событие.
     *
     * @param content Содержание нового события.
     * @param content Ссылка нового события.
     */
    fun addEvent(content: String, link: String, option: String, data: String)
}

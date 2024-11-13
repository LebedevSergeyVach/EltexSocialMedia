package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Event

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с событиями.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see InMemoryEventRepository Реализация интерфейса в памяти.
 */
interface EventRepository {

    /**
     * Возвращает Flow, который излучает список событий.
     *
     */
    fun getEvent(): Flow<List<Event>>

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
}
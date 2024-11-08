package com.eltex.androidschool.repository

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.data.Post
import kotlinx.coroutines.flow.Flow


/**
 * Интерфейс [EventRepository] предоставляет методы для управления событиями
 * Получить Собитие, поставить лайк и учавствовать
 */
interface EventRepository {
    /**
     * Получить событие в виде потока данных [Flow]
     *
     * @return [Flow] с данными о событии
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    fun getEvent(): Flow<Event>

    /**
     * Поставить лайк Событию пользователем
     * true & false
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    fun like()

    /**
     * Поставить участие Событию пользователем
     * true & false
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    fun participate()
}

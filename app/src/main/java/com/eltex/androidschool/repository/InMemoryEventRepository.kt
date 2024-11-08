package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Event

/**
 * Реализация интерфейса [EventRepository], которая хранит данные о Событии в памяти
 * Получить Событие, поставить лайк и учавстовать
 *
 * @property _state [MutableStateFlow] с данными о событии
 */
class InMemoryEventRepository : EventRepository {
    private val _state = MutableStateFlow(
        Event(
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            optionConducting = "Offline",
            dataEvent = "16.05.22 12:00",
            content = "Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
            link = "https://github.com/LebedevSergeyVach",
        )
    )

    /**
     * Получить событие в виде потока данных [Flow]
     *
     * @return [Flow] с данными о событии.
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    override fun getEvent(): Flow<Event> = _state.asStateFlow()

    /**
     * Поставить лайк Событию пользователем
     * true & false
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    override fun like() {
        _state.update { post ->
            post.copy(likeByMe = !post.likeByMe)
        }
    }

    /**
     * Поставить участие Событию пользователем
     * true & false
     *
     * @sample [EventRepository]
     * @sample [InMemoryEventRepository]
     */
    override fun participate() {
        _state.update { post ->
            post.copy(participateByMe = !post.participateByMe)
        }
    }
}

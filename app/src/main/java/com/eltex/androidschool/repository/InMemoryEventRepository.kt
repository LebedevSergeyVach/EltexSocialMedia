package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Event


class InMemoryEventRepository : EventRepository {
    private val _state = MutableStateFlow(
        List(20) { int ->
            Event(
                id = int.toLong(),
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                optionConducting = "Offline",
                dataEvent = "16.05.22 12:00",
                content = "№ ${int + 1} Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
                link = "https://github.com/LebedevSergeyVach",
            )
        }
            .reversed()
    )

    override fun getEvent(): Flow<List<Event>> = _state.asStateFlow()


    override fun likeById(eventId: Long) {
        _state.update { events: List<Event> ->
            events.map { event: Event ->
                if (event.id == eventId) {
                    event.copy(likeByMe = !event.likeByMe)
                } else {
                    event
                }
            }
        }
    }

    override fun participateById(eventId: Long) {
        _state.update { events: List<Event> ->
            events.map { event: Event ->
                if (event.id == eventId) {
                    event.copy(participateByMe = !event.participateByMe)
                } else {
                    event
                }
            }
        }
    }
}

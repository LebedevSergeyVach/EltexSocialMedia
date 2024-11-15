package com.eltex.androidschool.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.data.Event
import java.time.LocalDateTime

/**
 * Реализация интерфейса [EventRepository], хранящая данные о событиях в памяти.
 * Предоставляет методы для получения списка событий, лайков и участия в событиях.
 *
 * @see EventRepository Интерфейс, который реализует этот класс.
 */
class InMemoryEventRepository : EventRepository {

    /**
     * Flow, хранящий текущее состояние списка событий.
     * Инициализируется списком из 20 событий с фиктивными данными.
     */
    private val _state = MutableStateFlow(List(20) { int ->
        Event(
            id = int.toLong(),
            author = "Sergey Lebedev",
            published = LocalDateTime.now(),
            optionConducting = "Offline",
            dataEvent = "16.05.22 12:00",
            content = "\n№ ${int + 1} ❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F\n" + "Last Christmas, I gave you my heart\n" + "But the very next day, you gave it away\n" + "This year, to save me from tears\n" + "I'll give it to someone special\n" + "Last Christmas, I gave you my heart\n" + "But the very next day, you gave it away (You gave it away)\n" + "This year, to save me from tears\n" + "I'll give it to someone special (Special)",
            link = "https://github.com/LebedevSergeyVach",
        )
    }.reversed())

    /**
     * Возвращает Flow, который излучает список событий.
     *
     * @return Flow<List<Event>> Flow, излучающий список событий.
     */
    override fun getEvent(): Flow<List<Event>> = _state.asStateFlow()

    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     */
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

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     */
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

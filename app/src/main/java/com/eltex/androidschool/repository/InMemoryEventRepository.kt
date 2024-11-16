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
    private var nextId: Long = 10L

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
            content = "№ ${int + 1} ❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F☃\uFE0F❄\uFE0F\n" + "\nLast Christmas, I gave you my heart\n" + "But the very next day, you gave it away\n" + "This year, to save me from tears\n" + "I'll give it to someone special\n" + "Last Christmas, I gave you my heart\n" + "But the very next day, you gave it away (You gave it away)\n" + "This year, to save me from tears\n" + "I'll give it to someone special (Special)",
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

    /**
     * Удаления события по его id.
     *
     * @param eventId Идентификатор события, который нужно удалить.
     */
    override fun deleteById(eventId: Long) {
        _state.update { events: List<Event> ->
            events.filter { event: Event ->
                event.id != eventId
            }
        }
    }

    /**
     * Обновляет событие по его id.
     *
     * @param postId Идентификатор поста, который нужно обновить.
     * @param content Новое содержание события.
     * @param link Новая ссылка события.
     */
    override fun updateById(eventId: Long, content: String, link: String) {
        _state.update { events: List<Event> ->
            events.map { event: Event ->
                if (event.id == eventId) {
                    event.copy(content = content, link = link, lastModified = LocalDateTime.now())
                } else {
                    event
                }
            }
        }
    }

    /**
     * Добавляет новое событие.
     *
     * @param content Содержание нового события.
     * @param content Ссылка нового события.
     */
    override fun addEvent(content: String, link: String) {
        _state.update { events: List<Event> ->
            buildList(events.size + 1) {
                add(
                    Event(
                        id = nextId++,
                        content = content,
                        link = link,
                        author = "Student",
                        published = LocalDateTime.now()
                    )
                )
                addAll(events)
            }
        }
    }
}

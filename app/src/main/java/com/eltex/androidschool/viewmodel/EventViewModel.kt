package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.data.Event

/**
 * ViewModel для управления состоянием событий и взаимодействия с репозиторием.
 *
 * @param repository Репозиторий, который предоставляет данные о событиях.
 *
 * @see EventRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see EventState Состояние, которое управляется этим ViewModel.
 */
class EventViewModel(private val repository: EventRepository) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние событий.
     *
     * @see EventState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(EventState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию событий.
     *
     * @see EventState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<EventState> = _state.asStateFlow()

    /**
     * Инициализатор ViewModel.
     * Подписывается на изменения в Flow репозитория и обновляет состояние событий.
     */
    init {
        repository.getEvent()
            .onEach { events: List<Event> ->
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(events = events)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     */
    fun likeById(eventId: Long) {
        repository.likeById(eventId)
    }

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     */
    fun participateById(eventId: Long) {
        repository.participateById(eventId)
    }
}

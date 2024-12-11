package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.utils.Callback
import com.eltex.androidschool.utils.Logger

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        load()
    }

    /**
     * Загружает список событий с сервера.
     */
    fun load() {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusEvent.Loading
            )
        }

        repository.getEvents(
            object : Callback<List<EventData>> {
                override fun onSuccess(data: List<EventData>) {
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Idle,
                            events = data,
                        )
                    }
                }

                override fun onError(exception: Throwable) {
                    if (BuildConfig.DEBUG) {
                        Logger.e("Error: EventViewModel.load()", exception)
                    }

                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = exception)
                        )
                    }
                }
            }
        )
    }

    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     */
    fun likeById(eventId: Long, likedByMe: Boolean) {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusEvent.Loading
            )
        }

        repository.likeById(
            eventId = eventId,
            likedByMe = likedByMe,
            callback = object : Callback<EventData> {
                override fun onSuccess(data: EventData) {
                    updateEventInList(data)
                }

                override fun onError(exception: Throwable) {
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = exception)
                        )
                    }
                }
            }
        )
    }

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     */
    fun participateById(eventId: Long, participatedByMe: Boolean) {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusEvent.Loading
            )
        }

        repository.participateById(
            eventId = eventId,
            participatedByMe = participatedByMe,
            callback = object : Callback<EventData> {
                override fun onSuccess(data: EventData) {
                    updateEventInList(data)
                }

                override fun onError(exception: Throwable) {
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = exception)
                        )
                    }
                }
            }
        )
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     */
    fun deleteById(eventId: Long) {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusEvent.Loading
            )
        }

        repository.deleteById(
            eventId = eventId,
            callback = object : Callback<Unit> {
                override fun onSuccess(data: Unit) {
                    load()
                }

                override fun onError(exception: Throwable) {
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = exception)
                        )
                    }
                }
            }
        )
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusEvent.Idle
            )
        }
    }

    /**
     * Обновляет состояние списка событий, заменяя старое событие на обновленное.
     *
     * @param updatedEvent Обновленное событие.
     */
    private fun updateEventInList(updatedEvent: EventData) {
        _state.update { stateEvent: EventState ->
            val updatedEvents = stateEvent.events?.map { event: EventData ->
                if (event.id == updatedEvent.id) updatedEvent else event
            }

            stateEvent.copy(
                statusEvent = StatusEvent.Idle,
                events = updatedEvents
            )
        }
    }
}

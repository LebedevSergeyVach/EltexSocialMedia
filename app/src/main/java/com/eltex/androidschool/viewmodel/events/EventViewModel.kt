package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.events.EventUiModelMapper
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * ViewModel для управления состоянием событий и взаимодействия с репозиторием.
 *
 * Этот ViewModel отвечает за загрузку, обновление и управление состоянием событий.
 *
 * @param repository Репозиторий, который предоставляет данные о событиях.
 *
 * @see EventRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see EventState Состояние, которое управляется этим ViewModel.
 */
class EventViewModel(
    private val repository: EventRepository,
) : ViewModel() {

    /**
     * Маппер для преобразования данных события в UI-модель.
     *
     * @see EventUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = EventUiModelMapper()

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
                statusEvent = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val events: List<EventData> = repository.getEvents()

                val eventsUiModels: List<EventUiModel> = withContext(Dispatchers.Default) {
                    events.map { event: EventData ->
                        mapper.map(event)
                    }
                }

                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModels,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }


    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     */
    fun likeById(eventId: Long, likedByMe: Boolean) {
        viewModelScope.launch {
            try {
                val event: EventData = repository.likeById(
                    eventId = eventId,
                    likedByMe = likedByMe
                )

                val eventsUiModel: List<EventUiModel> = withContext(Dispatchers.Default) {
                    _state.value.events.orEmpty().map { eventUiModel: EventUiModel ->
                        if (eventUiModel.id == event.id) {
                            mapper.map(event)
                        } else {
                            eventUiModel
                        }
                    }
                }

                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     */
    fun participateById(eventId: Long, participatedByMe: Boolean) {
        viewModelScope.launch {
            try {
                val event: EventData = repository.participateById(
                    eventId = eventId,
                    participatedByMe = participatedByMe
                )

                val eventsUiModel: List<EventUiModel> = withContext(Dispatchers.Default) {
                    _state.value.events.orEmpty().map { eventUiModel: EventUiModel ->
                        if (eventUiModel.id == event.id) {
                            mapper.map(event)
                        } else {
                            eventUiModel
                        }
                    }
                }

                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Удаляет событие по его идентификатору.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     */
    fun deleteById(eventId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteById(eventId = eventId)

                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = _state.value.events.orEmpty().filter { event: EventUiModel ->
                            event.id != eventId
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { stateEvent: EventState ->
            stateEvent.copy(
                statusEvent = StatusLoad.Idle
            )
        }
    }

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с корутинами.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}

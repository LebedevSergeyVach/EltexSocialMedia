package com.eltex.androidschool.viewmodel.events.eventwall

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
import com.eltex.androidschool.ui.common.DateTimeUiFormatter
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.events.EventUiModelMapper
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

import java.time.ZoneId

/**
 * ViewModel для управления состоянием событий конкретного автора.
 * Загружает события, обрабатывает лайки, участие и удаление событий.
 *
 * @property repository Репозиторий для работы с событиями.
 * @property userId Идентификатор пользователя, чьи события загружаются (по умолчанию используется `BuildConfig.USER_ID`).
 * @see ViewModel
 */
@HiltViewModel(assistedFactory = EventWallViewModel.ViewModelFactory::class)
class EventWallViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    @Assisted private val userId: Long,
) : ViewModel() {

    /**
     * Маппер для преобразования данных события в UI-модель.
     *
     * @see EventUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = EventUiModelMapper(
        dateTimeUiFormatter = DateTimeUiFormatter(
            zoneId = ZoneId.systemDefault()
        )
    )

    /**
     * Flow, хранящий текущее состояние событий.
     *
     * @see EventWallState Состояние, которое хранится в этом Flow.
     */
    private val _state: MutableStateFlow<EventWallState> = MutableStateFlow(EventWallState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию событий.
     *
     * @see EventWallState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<EventWallState> = _state.asStateFlow()

    /**
     * Инициализатор ViewModel.
     * Подписывается на изменения в Flow репозитория и обновляет состояние событий.
     */
    init {
        loadEventsByAuthor(authorId = userId)
    }

    /**
     * Загружает события конкретного автора и обновляет состояние.
     *
     * @param authorId Идентификатор автора, чьи события нужно загрузить.
     * @see EventRepository.getEvents
     */
    fun loadEventsByAuthor(authorId: Long) {
        _state.update { stateEvent: EventWallState ->
            stateEvent.copy(
                statusEvent = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val events: List<EventData> = repository.getEvents()

                val filteredEvents = events.filter { event: EventData ->
                    event.authorId == authorId
                }

                val eventsUiModels: List<EventUiModel> = withContext(Dispatchers.Default) {
                    filteredEvents.map { event: EventData ->
                        mapper.map(event)
                    }
                }

                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModels,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Обрабатывает лайк события и обновляет состояние.
     *
     * @param eventId Идентификатор события.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь событие.
     * @see EventRepository.likeById
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

                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Обрабатывает участие в событии и обновляет состояние.
     *
     * @param eventId Идентификатор события.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в событии.
     * @see EventRepository.participateById
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

                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = eventsUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Удаляет событие по его идентификатору и обновляет состояние.
     *
     * @param eventId Идентификатор события.
     * @see EventRepository.deleteById
     */
    fun deleteById(eventId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteById(eventId = eventId)

                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Idle,
                        events = _state.value.events.orEmpty().filter { event: EventUiModel ->
                            event.id != eventId
                        }
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEvent: EventWallState ->
                    stateEvent.copy(
                        statusEvent = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Сбрасывает состояние ошибки и возвращает состояние в `Idle`.
     *
     * @see StatusEvent
     */
    fun consumerError() {
        _state.update { stateEvent: EventWallState ->
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

    @AssistedFactory
    interface ViewModelFactory {
        fun create(userId: Long): EventWallViewModel
    }
}

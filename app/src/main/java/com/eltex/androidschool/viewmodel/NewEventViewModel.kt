package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.data.EventData

import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управления созданием и обновлением событий.
 *
 * Этот ViewModel отвечает за сохранение нового события или обновление существующего.
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 * @see EventRepository Репозиторий для работы с данными событий.
 */
class NewEventViewModel(
    private val repository: EventRepository,
    private val eventId: Long = 0L,
) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние создания или обновления события.
     *
     * @see NewEventState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(NewEventState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию создания или обновления события.
     *
     * @see NewEventState Состояние, которое предоставляется этим Flow.
     */
    val state = _state.asStateFlow()

    /**
     * Сохраняет или обновляет событие.
     *
     * Если идентификатор события равен 0, создается новое событие.
     * В противном случае обновляется существующее событие.
     *
     * @param content Содержимое события.
     * @param link Ссылка на событие.
     * @param option Опция проведения события.
     * @param data Дата события.
     */
    fun save(content: String, link: String, option: String, data: String) {
        _state.update { newEventState: NewEventState ->
            newEventState.copy(
                statusEvent = StatusEvent.Loading
            )
        }

        repository.save(
            eventId = eventId,
            content = content,
            link = link,
            option = option,
            data = data,
            callback = object : Callback<EventData> {
                override fun onSuccess(data: EventData) {
                    _state.update { newEventState: NewEventState ->
                        newEventState.copy(
                            statusEvent = StatusEvent.Idle,
                            event = data,
                        )
                    }

                    EventViewModel(repository).load()
                }

                override fun onError(exception: Exception) {
                    _state.update { newEventState: NewEventState ->
                        newEventState.copy(
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
        _state.update { newEventState: NewEventState ->
            newEventState.copy(
                statusEvent = StatusEvent.Idle
            )
        }
    }
}

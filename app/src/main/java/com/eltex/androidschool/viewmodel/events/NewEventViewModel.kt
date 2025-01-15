package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.viewmodel.status.StatusLoad
import kotlinx.coroutines.cancel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для управления созданием и обновлением событий.
 *
 * Этот ViewModel отвечает за сохранение нового события или обновление существующего.
 *
 * @param repository Репозиторий для работы с данными событий.
 * @param eventId Идентификатор события (по умолчанию 0, если создается новое событие).
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
                statusEvent = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val event: EventData = repository.save(
                    eventId = eventId,
                    content = content,
                    link = link,
                    option = option,
                    data = data
                )

                _state.update { newEventState: NewEventState ->
                    newEventState.copy(
                        statusEvent = StatusLoad.Idle,
                        event = event,
                    )
                }
            } catch (e: Exception) {
                _state.update { newEventState: NewEventState ->
                    newEventState.copy(
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
        _state.update { newEventState: NewEventState ->
            newEventState.copy(
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

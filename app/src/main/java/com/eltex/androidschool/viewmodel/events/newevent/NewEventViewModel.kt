package com.eltex.androidschool.viewmodel.events.newevent

import android.content.ContentResolver
import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

import dagger.hilt.android.lifecycle.HiltViewModel

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
@HiltViewModel(assistedFactory = NewEventViewModel.ViewModelFactory::class)
class NewEventViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    @Assisted private val eventId: Long = 0L,
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
     * @param date Дата события.
     */
    fun save(
        content: String,
        link: String,
        option: String,
        date: String,
        contentResolver: ContentResolver,
        onProgress: (Int) -> Unit,
    ) {
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
                    date = date,
                    fileModel = _state.value.file,
                    contentResolver = contentResolver,
                    onProgress = onProgress,
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
     * Сохраняет тип файла вложения.
     *
     * Этот метод обновляет состояние ViewModel, сохраняя модель файла, который будет прикреплен к посту.
     *
     * @param file Модель файла, который будет прикреплен к посту. Может быть null, если вложение отсутствует.
     *
     * @see FileModel Класс, представляющий модель файла для вложения.
     */
    fun saveAttachmentFileType(file: FileModel?) {
        _state.update { stateNewEvent: NewEventState ->
            stateNewEvent.copy(
                file = file,
            )
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

    @AssistedFactory
    interface ViewModelFactory {
        fun create(eventId: Long = 0L): NewEventViewModel
    }
}

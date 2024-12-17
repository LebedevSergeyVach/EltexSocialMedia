package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.rx.common.SchedulersProvider

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управления созданием и обновлением событий.
 *
 * Этот ViewModel отвечает за сохранение нового события или обновление существующего.
 *
 * @param repository Репозиторий для работы с данными событий.
 * @param eventId Идентификатор события (по умолчанию 0, если создается новое событие).
 * @param schedulersProvider Провайдер для управления потоками выполнения.
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 * @see EventRepository Репозиторий для работы с данными событий.
 */
class NewEventViewModel(
    private val repository: EventRepository,
    private val eventId: Long = 0L,
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {

    /**
     * Композитный disposable для управления подписками RxJava.
     *
     * Используется для хранения всех подписок и их последующего освобождения при очистке ViewModel.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

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
            data = data
        )
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { newEvent: EventData ->
                    _state.update { newEventState: NewEventState ->
                        newEventState.copy(
                            statusEvent = StatusEvent.Idle,
                            event = newEvent,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { newEventState: NewEventState ->
                        newEventState.copy(
                            statusEvent = StatusEvent.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
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

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с подписками RxJava.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see CompositeDisposable Используется для управления подписками RxJava.
     */
    override fun onCleared() {
        disposable.dispose()
    }
}

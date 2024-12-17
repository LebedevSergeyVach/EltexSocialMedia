package com.eltex.androidschool.viewmodel.events

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.rx.common.SchedulersProvider
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.ui.events.EventUiModelMapper

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управления состоянием событий и взаимодействия с репозиторием.
 *
 * Этот ViewModel отвечает за загрузку, обновление и управление состоянием событий.
 *
 * @param repository Репозиторий, который предоставляет данные о событиях.
 * @param schedulersProvider Провайдер для управления потоками выполнения.
 *
 * @see EventRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see EventState Состояние, которое управляется этим ViewModel.
 */
class EventViewModel(
    private val repository: EventRepository,
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {

    /**
     * Маппер для преобразования данных события в UI-модель.
     *
     * @see EventUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = EventUiModelMapper()

    /**
     * Композитный disposable для управления подписками RxJava.
     *
     * Используется для хранения всех подписок и их последующего освобождения при очистке ViewModel.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

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

        repository.getEvents()
            .observeOn(schedulersProvider.computation)
            .map { events: List<EventData> ->
                events.map { event: EventData ->
                    mapper.map(event)
                }
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { allEvents: List<EventUiModel> ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Idle,
                            events = allEvents,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
    }


    /**
     * Помечает событие с указанным идентификатором как "лайкнутое" или "нелайкнутое".
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     */
    fun likeById(eventId: Long, likedByMe: Boolean) {
        repository.likeById(
            eventId = eventId,
            likedByMe = likedByMe
        )
            .observeOn(schedulersProvider.computation)
            .map { event: EventData ->
                _state.value.events.orEmpty().map { eventUiModel: EventUiModel ->
                    if (eventUiModel.id == event.id) {
                        mapper.map(event)
                    } else {
                        eventUiModel
                    }
                }
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { events: List<EventUiModel> ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Idle,
                            events = events,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
    }

    /**
     * Помечает событие с указанным идентификатором как "участие" или "отказ от участия".
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     */
    fun participateById(eventId: Long, participatedByMe: Boolean) {
        repository.participateById(
            eventId = eventId,
            participatedByMe = participatedByMe
        )
            .observeOn(schedulersProvider.computation)
            .map { event: EventData ->
                _state.value.events.orEmpty().map { eventUiModel: EventUiModel ->
                    if (eventUiModel.id == event.id) {
                        mapper.map(event)
                    } else {
                        eventUiModel
                    }
                }
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { events: List<EventUiModel> ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Idle,
                            events = events,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)

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

        repository.deleteById(eventId = eventId)
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onComplete = {
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Idle,
                            events = _state.value.events.orEmpty().filter { event: EventUiModel ->
                                event.id != eventId
                            }
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { stateEvent: EventState ->
                        stateEvent.copy(
                            statusEvent = StatusEvent.Error(throwable = throwable)
                        )
                    }
                },
            )
            .addTo(disposable)
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

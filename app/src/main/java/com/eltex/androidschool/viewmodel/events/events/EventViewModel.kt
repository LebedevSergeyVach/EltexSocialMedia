package com.eltex.androidschool.viewmodel.events.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * ViewModel для управления состоянием событий.
 * Этот класс взаимодействует с [EventStore] для получения состояния событий и обработки сообщений,
 * которые изменяют это состояние.
 *
 * @property eventStore Хранилище (Store), которое управляет состоянием событий и эффектами.
 * @see ViewModel
 */
class EventViewModel(
    private val eventStore: EventStore,
) : ViewModel() {

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию событий.
     *
     * @see EventState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<EventState> = eventStore.state

    /**
     * Инициализатор ViewModel.
     * При создании ViewModel запускается подключение к хранилищу (Store) для обработки сообщений и эффектов.
     */
    init {
        viewModelScope.launch {
            eventStore.connect()
        }
    }

    /**
     * Принимает сообщение и передает его в хранилище (Store) для обработки.
     * Этот метод используется для отправки сообщений, которые изменяют состояние событий,
     * таких как загрузка событий, лайки, участие и т.д.
     *
     * @param message Сообщение, которое нужно обработать.
     * @see EventMessage Типы сообщений, которые могут быть отправлены в хранилище.
     */
    fun accept(message: EventMessage) {
        eventStore.accept(message)
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

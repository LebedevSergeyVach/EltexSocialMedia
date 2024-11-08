package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.repository.PostRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управления состоянием События
 * Взаимодейтвует с [EventRepository] для получения и обновления данных о Событие
 *
 * @property repository Репозиторий Событий
 * @property state [StateFlow] с текущим состоянием События
 */
class EventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state: StateFlow<EventState> = _state.asStateFlow()

    /**
     * Инициализация ViewModel
     * Подписывается на изменения данных о Собитие из репозитория и обновляет состояние
     */
    init {
        repository.getEvent()
            .onEach { event ->
                _state.update { stateEvent ->
                    stateEvent.copy(event = event)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Поставить лайк Событию
     * Вызывает метод [PostRepository.like] для обновления состояния События
     */
    fun like() {
        repository.like()
    }

    /**
     * Учавствовать в Событие
     * Вызывает метод [PostRepository.participate] для обновления состояния События
     */
    fun participate()  {
        repository.participate()
    }
}

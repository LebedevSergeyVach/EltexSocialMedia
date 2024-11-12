package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.data.Event

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(EventState())
    val state: StateFlow<EventState> = _state.asStateFlow()

    init {
        repository.getEvent()
            .onEach { events: List<Event> ->
                _state.update { stateEvent: EventState ->
                    stateEvent.copy(events = events)
                }
            }
            .launchIn(viewModelScope)
    }

    fun likeById(eventId: Long) {
        repository.likeById(eventId)
    }

    fun participateById(eventId: Long) {
        repository.participateById(eventId)
    }
}

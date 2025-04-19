package com.eltex.androidschool.viewmodel.events.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.time.ZoneId

@HiltViewModel(assistedFactory = EventDetailsViewModel.ViewModelFactory::class)
class EventDetailsViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    @Assisted private val eventId: Long,
) : ViewModel() {
    private val _state: MutableStateFlow<EventDetailsState> = MutableStateFlow(EventDetailsState())
    val state: StateFlow<EventDetailsState> = _state.asStateFlow()

    private val mapper = EventUiModelMapper(
        dateTimeUiFormatter = DateTimeUiFormatter(
            zoneId = ZoneId.systemDefault()
        )
    )

    init {
        loadEvent(eventId = eventId)
    }

    fun loadEvent(eventId: Long) {
        _state.update { stateEventDetails: EventDetailsState ->
            stateEventDetails.copy(
                statusLoadEvent = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val event: EventData = repository.getEventById(eventId = eventId)

                val eventUiModel: EventUiModel = withContext(Dispatchers.Default) {
                    mapper.map(event = event, mapListAvatarModel = true)
                }

                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Success,
                        event = eventUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun likeById(eventId: Long, likedByMe: Boolean) {
        _state.update { stateEventDetails: EventDetailsState ->
            stateEventDetails.copy(
                statusLoadEvent = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val event: EventData = repository.likeById(eventId = eventId, likedByMe = likedByMe)

                val eventUiModel: EventUiModel = withContext(Dispatchers.Default) {
                    mapper.map(event = event, mapListAvatarModel = true)
                }

                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Idle,
                        event = eventUiModel,
                        isLikePressed = true,
                        isParticipatePressed = false,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun participationById(eventId: Long, participatedByMe: Boolean) {
        _state.update { stateEventDetails: EventDetailsState ->
            stateEventDetails.copy(
                statusLoadEvent = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val event: EventData = repository.participateById(
                    eventId = eventId,
                    participatedByMe = participatedByMe
                )

                val eventUiModel: EventUiModel = withContext(Dispatchers.Default) {
                    mapper.map(event = event, mapListAvatarModel = true)
                }

                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Idle,
                        event = eventUiModel,
                        isLikePressed = false,
                        isParticipatePressed = true,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateEventDetails: EventDetailsState ->
                    stateEventDetails.copy(
                        statusLoadEvent = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(eventId: Long): EventDetailsViewModel
    }
}

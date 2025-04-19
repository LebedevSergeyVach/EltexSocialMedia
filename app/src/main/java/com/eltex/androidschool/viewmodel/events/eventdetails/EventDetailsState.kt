package com.eltex.androidschool.viewmodel.events.eventdetails

import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

data class EventDetailsState(
    val event: EventUiModel? = null,
    val statusLoadEvent: StatusLoad = StatusLoad.Idle,
    val isLikePressed: Boolean = false,
    val isParticipatePressed: Boolean = false
) {

    val isEmptyLoading: Boolean
        get() = statusLoadEvent is StatusLoad.Loading && event == null

    val isEmptyError: Boolean
        get() = statusLoadEvent is StatusLoad.Error && event == null

    val isSuccessLoad: Boolean
        get() = statusLoadEvent is StatusLoad.Success && event != null
}

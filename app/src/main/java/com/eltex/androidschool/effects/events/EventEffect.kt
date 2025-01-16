package com.eltex.androidschool.effects.events

import com.eltex.androidschool.ui.events.EventUiModel

sealed interface EventEffect {
    data class LoadNextPage(val id: Long, val count: Int) : EventEffect
    data class LoadInitialPage(val count: Int) : EventEffect
    data class Like(val event: EventUiModel) : EventEffect
    data class Participation(val event: EventUiModel) : EventEffect
    data class Delete(val event: EventUiModel) : EventEffect
}

package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.data.events.EventData

/**
 * Состояние ViewModel для создания или обновления события.
 *
 * @property event Обновленное или созданное событие. По умолчанию null.
 * @property statusEvent Состояние операции. По умолчанию Idle.
 */
data class NewEventState(
    val event: EventData? = null,
    val statusEvent: StatusEvent = StatusEvent.Idle,
)

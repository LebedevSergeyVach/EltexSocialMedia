package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.EventData

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

package com.eltex.androidschool.model.events

import com.eltex.androidschool.ui.events.EventUiModel

/**
 * Класс, представляющий событие с ошибкой. Используется для хранения информации о событии,
 * которое вызвало ошибку, и самой ошибки.
 *
 * @property event Событие, связанное с ошибкой.
 * @property throwable Ошибка, которая произошла при выполнении операции с событием.
 */
data class EventWithError(
    val event: EventUiModel,
    val throwable: Throwable,
)

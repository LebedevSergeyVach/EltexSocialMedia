package com.eltex.androidschool.viewmodel.events

/**
 * Интерфейс, представляющий состояние операции.
 *
 * @property throwableOrNull Исключение, возникшее во время операции, если оно есть.
 */
interface StatusEvent {
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.exception

    /**
     * Состояние бездействия.
     */
    data object Idle : StatusEvent

    /**
     * Состояние загрузки.
     */
    data object Loading : StatusEvent

    /**
     * Состояние ошибки.
     *
     * @property exception Исключение, возникшее во время операции.
     */
    data class Error(val exception: Exception) : StatusEvent
}

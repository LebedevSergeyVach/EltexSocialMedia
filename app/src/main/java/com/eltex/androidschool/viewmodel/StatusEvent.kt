package com.eltex.androidschool.viewmodel

/**
 * Интерфейс, представляющий состояние операции.
 *
 * @property throwableOrNull Исключение, возникшее во время операции, если оно есть.
 */
interface StatusEvent {
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.throwable

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
     * @property throwable Исключение, возникшее во время операции.
     */
    data class Error(val throwable: Throwable) : StatusEvent
}

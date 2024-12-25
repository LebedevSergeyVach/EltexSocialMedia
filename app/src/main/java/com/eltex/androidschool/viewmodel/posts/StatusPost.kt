package com.eltex.androidschool.viewmodel.posts

/**
 * Интерфейс, представляющий состояние операции.
 *
 * @property throwableOrNull Исключение, возникшее во время операции, если оно есть.
 */
interface StatusPost {
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.exception

    /**
     * Состояние бездействия.
     */
    data object Idle : StatusPost

    /**
     * Состояние загрузки.
     */
    data object Loading : StatusPost

    /**
     * Состояние ошибки.
     *
     * @property exception Исключение, возникшее во время операции.
     */
    data class Error(val exception: Exception) : StatusPost
}

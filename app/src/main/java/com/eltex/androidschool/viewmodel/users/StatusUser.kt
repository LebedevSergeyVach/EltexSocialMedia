package com.eltex.androidschool.viewmodel.users

/**
 * Интерфейс, представляющий статус загрузки или ошибки для пользователей.
 */
interface StatusUser {

    /**
     * Возвращает исключение, если статус является ошибкой.
     */
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.exception

    /**
     * Статус, указывающий на отсутствие активности.
     */
    data object Idle : StatusUser

    /**
     * Статус, указывающий на загрузку данных.
     */
    data object Loading : StatusUser

    /**
     * Статус, указывающий на ошибку при загрузке данных.
     *
     * @property exception Исключение, возникшее при загрузке данных.
     */
    data class Error(val exception: Exception) : StatusUser
}

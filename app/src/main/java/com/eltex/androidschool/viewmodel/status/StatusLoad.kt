package com.eltex.androidschool.viewmodel.status

/**
 * Интерфейс для описания состояния загрузки данных.
 *
 * Этот интерфейс используется для представления различных состояний загрузки данных:
 * - `Idle`: Данные не загружаются.
 * - `Loading`: Данные загружаются.
 * - `Error`: Произошла ошибка при загрузке данных.
 *
 * @property throwableOrNull Возвращает исключение, если состояние `Error`, иначе `null`.
 */
interface StatusLoad {
    val throwableOrNull: Throwable?
        get() = (this as? Error)?.exception

    /**
     * Состояние, когда данные не загружаются.
     */
    data object Idle : StatusLoad

    /**
     * Состояние, когда данные загружаются.
     */
    data object Loading : StatusLoad

    /**
     * Состояние, когда произошла ошибка при загрузке данных.
     *
     * @property exception Исключение, вызвавшее ошибку.
     */
    data class Error(val exception: Exception) : StatusLoad
}

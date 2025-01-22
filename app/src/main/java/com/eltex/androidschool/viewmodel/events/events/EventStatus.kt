package com.eltex.androidschool.viewmodel.events.events

/**
 * Запечатанный интерфейс, представляющий различные состояния загрузки событий.
 * Этот интерфейс используется для отражения текущего состояния загрузки данных,
 * таких как начальная загрузка, обновление, загрузка следующей страницы и ошибки.
 */
sealed interface EventStatus {
    /**
     * Состояние, когда загрузка не выполняется и нет активных операций.
     */
    data class Idle(val loadingFinished: Boolean = false) : EventStatus

    /**
     * Состояние, когда выполняется обновление списка событий (например, при pull-to-refresh).
     */
    data object Refreshing : EventStatus

    /**
     * Состояние, когда выполняется начальная загрузка событий, но список событий пуст.
     */
    data object EmptyLoading : EventStatus

    /**
     * Состояние, когда выполняется загрузка следующей страницы событий.
     */
    data object NextPageLoading : EventStatus

    /**
     * Состояние, когда произошла ошибка при начальной загрузке событий, и список событий пуст.
     *
     * @property reason Исключение, которое вызвало ошибку.
     */
    data class EmptyError(val reason: Throwable) : EventStatus

    /**
     * Состояние, когда произошла ошибка при загрузке следующей страницы событий.
     *
     * @property reason Исключение, которое вызвало ошибку.
     */
    data class NextPageError(val reason: Throwable) : EventStatus
}

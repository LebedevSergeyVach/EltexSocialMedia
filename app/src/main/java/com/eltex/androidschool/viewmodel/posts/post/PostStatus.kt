package com.eltex.androidschool.viewmodel.posts.post

/**
 * Запечатанный интерфейс, представляющий различные состояния загрузки постов.
 * Этот интерфейс используется для отражения текущего состояния загрузки данных,
 * таких как начальная загрузка, обновление, загрузка следующей страницы и ошибки.
 */
sealed interface PostStatus {

    /**
     * Состояние, когда загрузка не выполняется и нет активных операций.
     */
    data object Idle : PostStatus

    /**
     * Состояние, когда выполняется обновление списка постов (например, при pull-to-refresh).
     */
    data object Refreshing : PostStatus

    /**
     * Состояние, когда выполняется начальная загрузка постов, но список постов пуст.
     */
    data object EmptyLoading : PostStatus

    /**
     * Состояние, когда выполняется загрузка следующей страницы постов.
     */
    data object NextPageLoading : PostStatus

    /**
     * Состояние, когда произошла ошибка при начальной загрузке постов, и список постов пуст.
     *
     * @property reason Исключение, которое вызвало ошибку.
     */
    data class EmptyError(val reason: Throwable) : PostStatus

    /**
     * Состояние, когда произошла ошибка при загрузке следующей страницы постов.
     *
     * @property reason Исключение, которое вызвало ошибку.
     */
    data class NextPageError(val reason: Throwable) : PostStatus
}
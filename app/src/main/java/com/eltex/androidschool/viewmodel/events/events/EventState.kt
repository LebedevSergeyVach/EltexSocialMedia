package com.eltex.androidschool.viewmodel.events.events

import com.eltex.androidschool.ui.events.EventUiModel

/**
 * Класс, представляющий состояние событий в приложении.
 * Этот класс хранит список событий, текущий статус загрузки и информацию об ошибках.
 *
 * @property events Список событий, отображаемых в UI.
 * @property statusEvent Текущий статус загрузки событий.
 * @property singleError Исключение, которое произошло при выполнении операции (например, лайк или удаление).
 */
data class EventState(
    val events: List<EventUiModel> = emptyList(),
    val statusEvent: EventStatus = EventStatus.Idle,
    val singleError: Throwable? = null,
) {
    /**
     * Флаг, указывающий, произошла ли ошибка при начальной загрузке событий.
     *
     * @return `true`, если статус загрузки — [EventStatus.EmptyError], иначе `false`.
     */
    val isEmptyError: Boolean = statusEvent is EventStatus.EmptyError

    /**
     * Флаг, указывающий, выполняется ли в данный момент обновление списка событий.
     *
     * @return `true`, если статус загрузки — [EventStatus.Refreshing], иначе `false`.
     */
    val isRefreshing: Boolean = statusEvent is EventStatus.Refreshing

    /**
     * Исключение, которое произошло при начальной загрузке событий.
     *
     * @return Исключение, если статус загрузки — [EventStatus.EmptyError], иначе `null`.
     */
    val emptyError: Throwable? = (statusEvent as? EventStatus.EmptyError)?.reason

    /**
     * Флаг, указывающий, выполняется ли в данный момент начальная загрузка событий, и список событий пуст.
     *
     * @return `true`, если статус загрузки — [EventStatus.EmptyLoading], иначе `false`.
     */
    val isEmptyLoading: Boolean = statusEvent is EventStatus.EmptyLoading
}

package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.EventData

/**
 * Состояние ViewModel для событий.
 * Содержит список событий, которые будут отображаться в UI.
 *
 * @property events Список событий, которые будут отображаться в UI. По умолчанию пустой список.
 * @property statusEvent Состояние операции. По умолчанию Idle.
 *
 * @sample [EventViewModel] Пример использования состояния в EventViewModel.
 */
data class EventState(
    val events: List<EventData>? = null,
    val statusEvent: StatusEvent = StatusEvent.Idle,
) {
    /**
     * Флаг, указывающий, что идет обновление списка событий.
     */
    val isRefreshing: Boolean
        get() = statusEvent == StatusEvent.Loading && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что идет загрузка списка событий.
     */
    val isEmptyLoading: Boolean
        get() = statusEvent == StatusEvent.Loading && events.isNullOrEmpty()

    /**
     * Флаг, указывающий, что произошла ошибка при обновлении списка событий.
     */
    val isRefreshError: Boolean
        get() = statusEvent is StatusEvent.Error && events?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что произошла ошибка при загрузке списка событий.
     */
    val isEmptyError: Boolean
        get() = statusEvent is StatusEvent.Error && events.isNullOrEmpty()
}

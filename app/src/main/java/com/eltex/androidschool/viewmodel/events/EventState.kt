package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.data.events.EventData

/**
 * Состояние ViewModel для событий.
 * Содержит список событий, которые будут отображаться в UI.
 *
 * @property events Список событий, которые будут отображаться в UI. По умолчанию пустой список.
 *
 * @sample [EventViewModel] Пример использования состояния в EventViewModel.
 */
data class EventState(
    val events: List<EventData> = emptyList(),
)

package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Event

/**
 * Состояние ViewModel для событий.
 * Содержит список событий, которые будут отображаться в UI.
 *
 * @property events Список событий, которые будут отображаться в UI. По умолчанию пустой список.
 *
 * @sample [EventViewModel] Пример использования состояния в EventViewModel.
 */
data class EventState(
    val events: List<Event> = emptyList(),
)

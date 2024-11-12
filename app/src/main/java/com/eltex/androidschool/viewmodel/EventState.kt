package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Event


data class EventState(
    val events: List<Event> = emptyList(),
)

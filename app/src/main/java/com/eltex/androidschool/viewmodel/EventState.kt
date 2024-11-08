package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Event


/**
 * Класс [EventState] представляет состояние Собития
 * Информация о Собитие, которая используется в ViewModel для обновления UI
 *
 * @property event Данные о Собитии
 */
data class EventState(
    val event: Event = Event(),
)

package com.eltex.androidschool.viewmodel.events.events

import com.eltex.androidschool.mvi.Store
import com.eltex.androidschool.effects.events.EventEffect

/**
 * Псевдоним типа для хранилища (Store), специализированного для работы с событиями.
 * Этот тип упрощает использование хранилища, связанного с состоянием событий, сообщениями и эффектами.
 *
 * @see Store Базовый класс хранилища, который управляет состоянием и эффектами.
 * @see EventState Состояние, связанное с событиями.
 * @see EventMessage Сообщения, которые могут изменять состояние событий.
 * @see EventEffect Эффекты, которые выполняются в ответ на сообщения.
 */
typealias EventStore = Store<EventState, EventMessage, EventEffect>

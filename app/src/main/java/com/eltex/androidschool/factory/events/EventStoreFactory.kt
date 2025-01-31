package com.eltex.androidschool.factory.events

import com.eltex.androidschool.effecthandler.events.EventEffectHandler
import com.eltex.androidschool.reducer.events.EventReducer
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.ui.events.EventUiModelMapper
import com.eltex.androidschool.viewmodel.events.events.EventMessage
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventStore

/**
 * Фабрика для создания хранилищ (Store) связанных с событиями.
 * Этот класс отвечает за создание экземпляров [EventStore],
 * которые управляют состоянием и логикой для отображения событий.
 *
 * @property repository Репозиторий для работы с данными событий.
 *
 * @see EventStore
 * @see EventRepository
 */
class EventStoreFactory(
    private val repository: EventRepository,
) {
    /**
     * Создает экземпляр [EventStore], который управляет состоянием и логикой для отображения событий.
     *
     * @return [EventStore] Экземпляр хранилища для управления состоянием событий.
     *
     * @see EventStore
     * @see EventReducer
     * @see EventEffectHandler
     * @see EventMessage
     * @see EventState
     */
    fun createEventFactory(): EventStore = EventStore(
        reducer = EventReducer(),
        effectHandler = EventEffectHandler(
            repository = repository,
            mapper = EventUiModelMapper(),
        ),
        initMessages = setOf(EventMessage.Refresh),
        initState = EventState()
    )
}

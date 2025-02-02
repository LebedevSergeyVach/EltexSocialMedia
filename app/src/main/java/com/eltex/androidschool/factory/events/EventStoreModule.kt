package com.eltex.androidschool.factory.events

import com.eltex.androidschool.effecthandler.events.EventEffectHandler
import com.eltex.androidschool.reducer.events.EventReducer
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.viewmodel.events.events.EventMessage
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventStore

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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
@Module
@InstallIn(ViewModelComponent::class)
class EventStoreModule {

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
    @Provides
    fun createEventFactory(
        reducer: EventReducer,
        effectHandler: EventEffectHandler,
    ): EventStore = EventStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(EventMessage.Refresh),
        initState = EventState()
    )
}

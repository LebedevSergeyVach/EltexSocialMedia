package com.eltex.androidschool.effecthandler.events

import arrow.core.Either
import arrow.core.left
import arrow.core.right

import com.eltex.androidschool.effects.events.EventEffect
import com.eltex.androidschool.model.events.EventWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.events.NetworkEventRepository
import com.eltex.androidschool.ui.events.EventUiModelMapper
import com.eltex.androidschool.viewmodel.events.events.EventMessage

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

/**
 * Обработчик эффектов для событий. Этот класс реализует интерфейс [EffectHandler] и отвечает за
 * обработку различных эффектов, таких как загрузка событий, лайки, участие и удаление.
 *
 * @param repository Репозиторий для работы с событиями в сети.
 * @param mapper Маппер для преобразования моделей данных в UI-модели.
 */
class EventEffectHandler(
    private val repository: NetworkEventRepository,
    private val mapper: EventUiModelMapper,
) : EffectHandler<EventEffect, EventMessage> {

    /**
     * Подключает поток эффектов к потоку сообщений. Этот метод объединяет обработчики для всех типов эффектов.
     *
     * @param effects Поток эффектов, которые нужно обработать.
     * @return Поток сообщений, которые будут отправлены в хранилище (Store).
     */
    override fun connect(effects: Flow<EventEffect>): Flow<EventMessage> =
        listOf(
            handleNextPage(effects = effects),
            handleInitialPage(effects = effects),
            handleLikeEvent(effects = effects),
            handleParticipationEvent(effects = effects),
            handleDeleteEvent(effects = effects),
        )
            .merge()

    /**
     * Обрабатывает эффект загрузки следующей страницы событий.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [EventEffect.LoadNextPage].
     * @return Поток сообщений, содержащий результат загрузки следующей страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleNextPage(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.LoadNextPage>()
            .mapLatest { eventEffect: EventEffect.LoadNextPage ->
                EventMessage.NextPageLoaded(
                    try {
                        repository.getBeforeEvents(
                            id = eventEffect.id,
                            count = eventEffect.count,
                        )
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    /**
     * Обрабатывает эффект загрузки начальной страницы событий.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [EventEffect.LoadInitialPage].
     * @return Поток сообщений, содержащий результат загрузки начальной страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleInitialPage(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.LoadInitialPage>()
            .mapLatest { eventEffect: EventEffect.LoadInitialPage ->
                EventMessage.InitialLoaded(
                    try {
                        repository.getLatestEvents(
                            count = eventEffect.count
                        )
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    /**
     * Обрабатывает эффект лайка события.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [EventEffect.Like].
     * @return Поток сообщений, содержащий результат лайка события.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleLikeEvent(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Like>()
            .mapLatest { eventEffect: EventEffect.Like ->
                EventMessage.LikeResult(
                    try {
                        Either.Right(
                            mapper.map(
                                repository.likeById(
                                    eventId = eventEffect.event.id,
                                    likedByMe = eventEffect.event.likedByMe
                                )
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(EventWithError(event = eventEffect.event, throwable = e))
                    }
                )
            }

    /**
     * Обрабатывает эффект участия в событии.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [EventEffect.Participation].
     * @return Поток сообщений, содержащий результат участия в событии.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleParticipationEvent(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Participation>()
            .mapLatest { eventEffect: EventEffect.Participation ->
                EventMessage.ParticipationResult(
                    try {
                        Either.Right(
                            mapper.map(
                                repository.participateById(
                                    eventId = eventEffect.event.id,
                                    participatedByMe = eventEffect.event.participatedByMe
                                )
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(EventWithError(event = eventEffect.event, throwable = e))
                    }
                )
            }

    /**
     * Обрабатывает эффект удаления события.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [EventEffect.Delete].
     * @return Поток сообщений, содержащий ошибку удаления, если она произошла.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleDeleteEvent(effects: Flow<EventEffect>) =
        effects.filterIsInstance<EventEffect.Delete>()
            .mapLatest { eventEffect: EventEffect.Delete ->
                try {
                    repository.deleteById(eventId = eventEffect.event.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    EventMessage.DeleteError(
                        EventWithError(
                            event = eventEffect.event,
                            throwable = e
                        )
                    )
                }
            }
            .filterIsInstance<EventMessage.DeleteError>()
}

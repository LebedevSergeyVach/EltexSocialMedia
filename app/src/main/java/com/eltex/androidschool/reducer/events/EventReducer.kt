package com.eltex.androidschool.reducer.events

import arrow.core.Either

import com.eltex.androidschool.effects.events.EventEffect
import com.eltex.androidschool.model.events.EventWithError
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.viewmodel.events.events.EventMessage
import com.eltex.androidschool.viewmodel.events.events.EventState
import com.eltex.androidschool.viewmodel.events.events.EventStatus

import javax.inject.Inject

/**
 * Редьюсер для событий. Этот класс реализует интерфейс [Reducer] и отвечает за обработку сообщений,
 * связанных с событиями, и обновление состояния приложения.
 *
 * @see Reducer Интерфейс, который реализует этот класс.
 * @see EventState Состояние, связанное с событиями в фрагменте со списком событий.
 * @see EventEffect Сообщения, которые могут изменять состояние событий.
 * @see EventMessage Эффекты, которые выполняются в ответ на сообщения.
 */
class EventReducer @Inject constructor() : Reducer<EventState, EventEffect, EventMessage> {

    companion object {
        /**
         * Размер страницы для пагинации. Определяет, сколько постов загружается за один запрос.
         */
        const val PAGE_SIZE: Int = 10
    }

    /**
     * Обрабатывает сообщение и возвращает новое состояние и набор эффектов, которые нужно выполнить.
     *
     * @param old Текущее состояние приложения.
     * @param message Сообщение, которое нужно обработать.
     * @return Результат обработки сообщения, содержащий новое состояние и набор эффектов.
     */
    override fun reduce(
        old: EventState,
        message: EventMessage
    ): ReducerResult<EventState, EventEffect> =
        when (message) {
            is EventMessage.Delete -> ReducerResult(
                newState = old.copy(events = old.events.filter { event: EventUiModel ->
                    event.id != message.event.id
                }),

                action = EventEffect.Delete(message.event)
            )

            is EventMessage.DeleteError -> ReducerResult(
                newState = old.copy(
                    events = buildList(old.events.size + 1) {
                        val deletedEvent: EventUiModel = message.error.event

                        addAll(old.events.filter { eventInState: EventUiModel ->
                            eventInState.id > deletedEvent.id
                        })

                        add(deletedEvent)

                        addAll(old.events.filter { eventInState: EventUiModel ->
                            eventInState.id < deletedEvent.id
                        })
                    }
                )
            )

            EventMessage.HandleError -> ReducerResult(
                newState = old.copy(
                    singleError = null
                )
            )

            is EventMessage.InitialLoaded -> ReducerResult(
                newState = when (
                    val messageResult: Either<Throwable, List<EventUiModel>> = message.result
                ) {
                    is Either.Right -> old.copy(
                        events = messageResult.value,
                        statusEvent = EventStatus.Idle(),
                    )

                    is Either.Left -> {
                        if (old.events.isNotEmpty()) {
                            old.copy(
                                singleError = messageResult.value,
                                statusEvent = EventStatus.Idle()
                            )
                        } else {
                            old.copy(
                                statusEvent = EventStatus.EmptyError(reason = messageResult.value)
                            )
                        }
                    }
                }
            )

            is EventMessage.Like -> ReducerResult(
                newState = old.copy(
                    events = old.events.map { event: EventUiModel ->
                        if (event.id == message.event.id) {
                            event.copy(
                                likedByMe = !event.likedByMe,
                                likes = if (event.likedByMe) event.likes - 1 else event.likes + 1
                            )
                        } else {
                            event
                        }
                    }
                ),

                action = EventEffect.Like(message.event)
            )

            is EventMessage.LikeResult -> ReducerResult(
                newState = when (val result = message.result) {
                    is Either.Right -> {
                        val eventLiked: EventUiModel = result.value

                        old.copy(
                            events = old.events.map { event: EventUiModel ->
                                if (event.id == eventLiked.id) {
                                    eventLiked
                                } else {
                                    event
                                }
                            }
                        )
                    }

                    is Either.Left -> {
                        val value: EventWithError = result.value
                        val eventLiked: EventUiModel = value.event

                        old.copy(
                            events = old.events.map { event: EventUiModel ->
                                if (event.id == eventLiked.id) {
                                    eventLiked
                                } else {
                                    event
                                }
                            },

                            singleError = value.throwable
                        )
                    }
                }
            )

            EventMessage.LoadNextPage -> {
                val loadingFinished: Boolean =
                    (old.statusEvent as? EventStatus.Idle)?.loadingFinished == true

                val status: EventStatus =
                    if (loadingFinished || old.statusEvent !is EventStatus.Idle) {
                        old.statusEvent
                    } else {
                        EventStatus.NextPageLoading
                    }

                val effect: EventEffect.LoadNextPage? = if (loadingFinished) {
                    null
                } else {
                    EventEffect.LoadNextPage(
                        id = old.events.last().id,
                        count = PAGE_SIZE
                    )
                }

                ReducerResult(
                    newState = old.copy(
                        statusEvent = status
                    ),

                    action = effect
                )
            }

            is EventMessage.NextPageLoaded -> ReducerResult(
                newState = when (val messageResult = message.result) {
                    is Either.Right -> {
                        val eventUiModels: List<EventUiModel> = messageResult.value
                        val loadingFinished: Boolean = eventUiModels.size < PAGE_SIZE

                        old.copy(
                            statusEvent = EventStatus.Idle(loadingFinished = loadingFinished),
                            events = old.events + messageResult.value
                        )
                    }

                    is Either.Left -> {
                        old.copy(
                            statusEvent = EventStatus.NextPageError(reason = messageResult.value)
                        )
                    }
                }
            )

            is EventMessage.Participation -> ReducerResult(
                newState = old.copy(
                    events = old.events.map { event: EventUiModel ->
                        if (event.id == message.event.id) {
                            event.copy(
                                participatedByMe = !event.participatedByMe,
                                participates = if (event.participatedByMe) event.participates - 1 else event.participates + 1
                            )
                        } else {
                            event
                        }
                    }
                ),

                action = EventEffect.Participation(message.event)
            )

            is EventMessage.ParticipationResult -> ReducerResult(
                newState = when (val result = message.result) {
                    is Either.Right -> {
                        val eventParticipation: EventUiModel = result.value

                        old.copy(
                            events = old.events.map { event: EventUiModel ->
                                if (event.id == eventParticipation.id) {
                                    eventParticipation
                                } else {
                                    event
                                }
                            }
                        )
                    }

                    is Either.Left -> {
                        val value: EventWithError = result.value
                        val eventParticipation: EventUiModel = value.event

                        old.copy(
                            events = old.events.map { event: EventUiModel ->
                                if (event.id == eventParticipation.id) {
                                    eventParticipation
                                } else {
                                    event
                                }
                            },

                            singleError = value.throwable
                        )
                    }
                }
            )

            EventMessage.Retry -> {
                val nextId: Long? = old.events.lastOrNull()?.id

                if (nextId == null) {
                    ReducerResult(old)
                } else {
                    ReducerResult(
                        newState = old.copy(
                            statusEvent = EventStatus.NextPageLoading,
                        ),

                        action = EventEffect.LoadNextPage(
                            id = nextId,
                            count = PAGE_SIZE
                        )
                    )
                }
            }

            EventMessage.Refresh -> ReducerResult(
                newState = old.copy(
                    statusEvent = if (old.events.isNotEmpty()) {
                        EventStatus.Refreshing
                    } else {
                        EventStatus.EmptyLoading
                    }
                ),

                action = EventEffect.LoadInitialPage(count = PAGE_SIZE)
            )
        }
}

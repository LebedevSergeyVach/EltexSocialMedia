package com.eltex.androidschool.mvi

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

/**
 * Класс, представляющий хранилище (Store) в архитектуре MVI. Хранилище управляет состоянием приложения
 * и обрабатывает сообщения, которые могут изменять состояние.
 *
 * @param reducer Редьюсер, который обрабатывает сообщения и обновляет состояние.
 * @param effectHandler Обработчик эффектов, который выполняет побочные эффекты.
 * @param initMessages Набор начальных сообщений, которые будут отправлены при запуске хранилища.
 * @param initState Начальное состояние приложения.
 */
class Store<State, Message, Effect>(
    private val reducer: Reducer<State, Effect, Message>,
    private val effectHandler: EffectHandler<Effect, Message>,
    private val initMessages: Set<Message> = emptySet(),
    initState: State,
) {

    /**
     * Приватный поток для хранения и обновления текущего состояния приложения.
     * Используется для управления состоянием в архитектуре MVI.
     *
     * @property _state Хранит текущее состояние приложения, которое может включать данные (например, список постов), статус загрузки, ошибки и другие параметры. Состояние обновляется через редьюсер в ответ на сообщения (Intents).
     * @see MutableStateFlow Поток, который позволяет обновлять и читать текущее состояние.
     * @see State Состояние приложения, которое может быть сериализовано и восстановлено.
     */
    private val _state = MutableStateFlow(initState)

    /**
     * Публичный поток для предоставления текущего состояния компонентам UI.
     * Компоненты UI могут подписаться на этот поток и обновлять интерфейс в соответствии с текущим состоянием.
     *
     * @property state Предоставляет доступ к текущему состоянию приложения. Является неизменяемым потоком данных, что предотвращает изменение состояния извне.
     * @see StateFlow Поток, который предоставляет доступ только для чтения к текущему состоянию.
     * @see State Состояние приложения, которое может быть сериализовано и восстановлено.
     */
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * Приватный поток для обработки пользовательских действий (Intents).
     * Используется для передачи намерений пользователя (например, лайк или удаление поста) в ViewModel,
     * где они обрабатываются и преобразуются в изменения состояния.
     *
     * @property messages Поток сообщений, которые могут быть отправлены пользователем. Сообщения обрабатываются асинхронно, что позволяет избежать блокировки основного потока.
     * @see MutableSharedFlow Поток, который позволяет отправлять и получать сообщения асинхронно.
     * @see Message Тип сообщений, которые могут быть отправлены пользователем.
     */
    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 64)

    /**
     * Приватный поток для выполнения побочных эффектов (например, загрузка данных с сервера, показ уведомлений).
     * Используется для выполнения действий, которые не связаны напрямую с изменением состояния, но необходимы
     * для корректной работы приложения.
     *
     * @property effects Поток побочных эффектов, которые могут быть выполнены. Эффекты обрабатываются асинхронно, что позволяет избежать блокировки основного потока.
     * @see MutableSharedFlow Поток, который позволяет отправлять и получать эффекты асинхронно.
     * @see Effect Тип побочных эффектов, которые могут быть выполнены.
     */
    private val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 64)

    /**
     * Принимает сообщение и добавляет его в поток сообщений для обработки.
     *
     * @param message Сообщение, которое нужно обработать.
     */
    fun accept(message: Message) {
        messages.tryEmit(message)
    }

    /**
     * Запускает хранилище и начинает обработку сообщений и эффектов.
     */
    suspend fun connect() = coroutineScope {
        launch {
            effectHandler.connect(effects)
                .collect(messages::tryEmit)
        }
        launch {
            listOf(
                initMessages.asFlow(),
                messages,
            )
                .merge()
                .map { message: Message ->
                    reducer.reduce(_state.value, message)
                }
                .collect { reducerResult: ReducerResult<State, Effect> ->
                    _state.value = reducerResult.newState
                    reducerResult.effects.forEach(effects::tryEmit)
                }
        }
    }
}

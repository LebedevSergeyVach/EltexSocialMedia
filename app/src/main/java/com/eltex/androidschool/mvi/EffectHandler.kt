package com.eltex.androidschool.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для обработки эффектов. Эффекты представляют собой действия, которые должны быть выполнены
 * в ответ на сообщения, такие как загрузка данных, лайки и т.д.
 *
 * @param Effect Тип эффекта, который будет обрабатываться.
 * @param Message Тип сообщения, которое будет возвращено после обработки эффекта.
 */
interface EffectHandler<Effect, Message> {

    /**
     * Подключает поток эффектов к потоку сообщений.
     *
     * @param effects Поток эффектов, которые нужно обработать.
     * @return Поток сообщений, которые будут отправлены в хранилище (Store).
     */
    fun connect(effects: Flow<Effect>): Flow<Message>
}

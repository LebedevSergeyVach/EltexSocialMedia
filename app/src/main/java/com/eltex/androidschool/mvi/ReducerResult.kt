package com.eltex.androidschool.mvi

/**
 * Класс, представляющий результат работы редьюсера. Содержит новое состояние и набор эффектов,
 * которые нужно выполнить.
 *
 * @param newState Новое состояние приложения.
 * @param effects Набор эффектов, которые нужно выполнить.
 */
data class ReducerResult<State, Effect>(
    val newState: State,
    val effects: Set<Effect>,
) {

    /**
     * Альтернативный конструктор для создания результата с одним эффектом.
     *
     * @param newState Новое состояние приложения.
     * @param action Единственный эффект, который нужно выполнить.
     */
    constructor(newState: State, action: Effect? = null) : this(newState, setOfNotNull(action))
}
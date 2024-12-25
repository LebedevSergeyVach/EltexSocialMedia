package com.eltex.androidschool

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Правило для управления корутинами в тестах.
 *
 * Это правило используется для замены основного диспетчера (`Dispatchers.Main`) на тестовый диспетчер (`TestDispatcher`)
 * во время выполнения тестов. Это позволяет контролировать выполнение корутин в тестах и избегать проблем с асинхронным кодом.
 *
 * @see TestDispatcher Тестовый диспетчер, используемый для управления корутинами в тестах.
 * @see ExperimentalCoroutinesApi Аннотация, указывающая, что используются экспериментальные API корутин.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutinesRule : TestWatcher() {
    /**
     * Тестовый диспетчер, используемый для управления корутинами в тестах.
     *
     * Этот диспетчер позволяет контролировать выполнение корутин в тестах, чтобы избежать проблем с асинхронным кодом.
     * В данном случае используется `UnconfinedTestDispatcher`, который выполняет корутины немедленно, без каких-либо ограничений.
     *
     * @see UnconfinedTestDispatcher Тестовый диспетчер, который выполняет корутины немедленно.
     */
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    /**
     * Метод, вызываемый перед началом выполнения теста.
     *
     * Этот метод заменяет основной диспетчер (`Dispatchers.Main`) на тестовый диспетчер (`TestDispatcher`).
     * Это позволяет контролировать выполнение корутин в тестах и избегать проблем с асинхронным кодом.
     *
     * @param description Описание теста, которое может быть использовано для отладки.
     * @see Dispatchers.setMain Метод, который заменяет основной диспетчер на тестовый.
     */
    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
    }

    /**
     * Метод, вызываемый после завершения выполнения теста.
     *
     * Этот метод восстанавливает основной диспетчер (`Dispatchers.Main`) после завершения теста.
     * Это важно для того, чтобы другие тесты или основная программа продолжали использовать правильный диспетчер.
     *
     * @param description Описание теста, которое может быть использовано для отладки.
     * @see Dispatchers.resetMain Метод, который восстанавливает основной диспетчер.
     */
    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}

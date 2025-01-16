package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.TestCoroutinesRule
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.TestEventRepository
import com.eltex.androidschool.viewmodel.events.newevent.NewEventState
import com.eltex.androidschool.viewmodel.events.newevent.NewEventViewModel

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.Instant

/**
 * Тестовый класс для проверки функциональности ViewModel, отвечающего за создание и обновление событий.
 *
 * Этот класс содержит тесты для проверки корректной работы методов `NewEventViewModel`, таких как:
 * - Сохранение нового события.
 * - Обновление существующего события.
 * - Обработка ошибок при выполнении операций.
 *
 * Тесты используют моки репозитория для имитации поведения сервера и проверки корректности обновления состояния ViewModel.
 *
 * @see NewEventViewModel ViewModel, которое тестируется.
 * @see TestEventRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestCoroutinesRule Правило для управления корутинами в тестах.
 */
class NewEventViewModelTest {

    /**
     * Правило для управления корутинами в тестах.
     *
     * Это правило заменяет основной диспетчер (`Dispatchers.Main`) на тестовый диспетчер (`TestDispatcher`)
     * перед выполнением теста и восстанавливает его после завершения теста. Это позволяет контролировать
     * выполнение корутин в тестах и избегать проблем с асинхронным кодом.
     *
     * Используется в тестах, где необходимо управлять корутинами, чтобы гарантировать корректное выполнение
     * асинхронных операций.
     *
     * @see TestCoroutinesRule Класс, реализующий правило для управления корутинами в тестах.
     */
    @get:Rule
    val coroutinesRule: TestCoroutinesRule = TestCoroutinesRule()

    /**
     * Тест для проверки успешного сохранения нового события.
     */
    @Test
    fun `save new event successfully`() {
        val eventId = 0L
        val content = "New Event Content"
        val link = "link.ru"
        val option = "OFFLINE"
        val data: Instant = Instant.now()

        val newEvent = EventData(
            id = eventId,
            content = content,
            link = link,
            optionConducting = option,
            dataEvent = data,
        )

        val viewModel = NewEventViewModel(
            object : TestEventRepository {
                override suspend fun save(
                    eventId: Long,
                    content: String,
                    link: String,
                    option: String,
                    data: String
                ): EventData = newEvent
            },
            eventId = eventId
        )

        val expected = NewEventState(
            event = newEvent,
            statusEvent = StatusEvent.Idle
        )

        viewModel.save(
            content = content,
            link = link,
            option = option,
            data = data.toString()
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при сохранении нового события.
     */
    @Test
    fun `save new event with error`() {
        val error = RuntimeException("test error save")

        val eventId = 0L
        val content = "New Event Content"
        val link = "link.ru"
        val option = "OFFLINE"
        val data: Instant = Instant.now()

        val viewModel = NewEventViewModel(
            object : TestEventRepository {
                override suspend fun save(
                    eventId: Long,
                    content: String,
                    link: String,
                    option: String,
                    data: String
                ): EventData = throw error
            },
            eventId = eventId
        )

        val expected = NewEventState(
            event = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.save(
            content = content,
            link = link,
            option = option,
            data = data.toString()
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки успешного обновления существующего события.
     */
    @Test
    fun `update existing event successfully`() {
        val eventId = 1L
        val content = "Updated Event Content"
        val link = "link.ru"
        val option = "OFFLINE"
        val data: Instant = Instant.now()

        val updateEvent = EventData(
            id = eventId,
            content = content,
            link = link,
            optionConducting = option,
            dataEvent = data,
        )

        val viewModel = NewEventViewModel(
            object : TestEventRepository {
                override suspend fun save(
                    eventId: Long,
                    content: String,
                    link: String,
                    option: String,
                    data: String
                ): EventData = updateEvent
            },
            eventId = eventId
        )

        val expected = NewEventState(
            event = updateEvent,
            statusEvent = StatusEvent.Idle
        )

        viewModel.save(
            content = content,
            link = link,
            option = option,
            data = data.toString()
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при обновлении существующего события.
     */
    @Test
    fun `update existing event with error`() {
        val error = RuntimeException("test error update")

        val eventId = 1L
        val content = "Updated Event Content"
        val link = "link.ru"
        val option = "OFFLINE"
        val data: Instant = Instant.now()

        val viewModel = NewEventViewModel(
            object : TestEventRepository {
                override suspend fun save(
                    eventId: Long,
                    content: String,
                    link: String,
                    option: String,
                    data: String
                ): EventData = throw error
            },
            eventId = eventId
        )

        val expected = NewEventState(
            event = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.save(
            content = content,
            link = link,
            option = option,
            data = data.toString()
        )

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }
}

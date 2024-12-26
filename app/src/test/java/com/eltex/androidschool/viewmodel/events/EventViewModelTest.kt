package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.TestCoroutinesRule
import com.eltex.androidschool.TestUtils.loading

import com.eltex.androidschool.repository.TestEventRepository
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.ui.events.EventUiModelMapper

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Тестовый класс для проверки функциональности ViewModel, отвечающего за управление событиями.
 *
 * Этот класс содержит тесты для проверки корректной работы методов `EventViewModel`, таких как:
 * - Загрузка списка событий.
 * - Постановка и снятие лайка у события.
 * - Участие и отказ от участия в событии.
 * - Удаление события.
 * - Обработка ошибок при выполнении операций.
 *
 * Тесты используют моки репозитория для имитации поведения сервера и проверки корректности обновления состояния ViewModel.
 *
 * @see EventViewModel ViewModel, которое тестируется.
 * @see TestEventRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestCoroutinesRule Правило для управления корутинами в тестах.
 */
class EventViewModelTest {

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
     * Маппер для преобразования данных поста в UI-модель.
     *
     * @see EventUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = EventUiModelMapper()

    /**
     * Тест для проверки корректной загрузки списка событий.
     */
    @Test
    fun `load events successfully`() {
        val events = listOf(
            EventData(id = 1, content = "Event 1"),
            EventData(id = 2, content = "Event 2")
        )

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun getEvents(): List<EventData> = events
            }
        )

        val expected = EventState(
            events = events.map { event: EventData ->
                mapper.map(event)
            },
            statusEvent = StatusEvent.Idle
        )

        viewModel.load()

        loading()

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при загрузке списка событий.
     */
    @Test
    fun `load events with error`() {
        val error = RuntimeException("test error load")

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun getEvents(): List<EventData> = throw error
            }
        )

        val expected = EventState(
            events = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.load()

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного лайка события.
     */
    @Test
    fun `like event successfully`() {
        val eventId = 1L

        val initialEvents = listOf(
            EventData(id = eventId, content = "Event 1", likedByMe = false),
            EventData(id = 2, content = "Event 2", likedByMe = false)
        )

        val likedEvent = EventData(id = eventId, content = "Event 1", likedByMe = true)

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun getEvents(): List<EventData> = initialEvents
                override suspend fun likeById(eventId: Long, likedByMe: Boolean): EventData =
                    likedEvent
            }
        )

        val expected = EventState(
            events = initialEvents.map { event: EventData ->
                mapper.map(event)
            },
            statusEvent = StatusEvent.Idle
        )

        viewModel.load()

        loading()

        viewModel.likeById(eventId, likedEvent.likedByMe)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при лайке события.
     */
    @Test
    fun `like event with error`() {
        val error = RuntimeException("test error like")

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun likeById(eventId: Long, likedByMe: Boolean): EventData =
                    throw error
            }
        )

        val expected = EventState(
            events = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.likeById(1, false)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного участия в событии.
     */
    @Test
    fun `participate in event successfully`() {
        val eventId = 1L

        val initialEvents = listOf(
            EventData(id = eventId, content = "Event 1", participatedByMe = false),
            EventData(id = 2, content = "Event 2", participatedByMe = false)
        )

        val participatedEvent =
            EventData(id = eventId, content = "Event 1", participatedByMe = true)

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun getEvents(): List<EventData> = initialEvents
                override suspend fun participateById(
                    eventId: Long,
                    participatedByMe: Boolean
                ): EventData = participatedEvent
            }
        )

        val expected = EventState(
            events = initialEvents.map { event: EventData ->
                mapper.map(event)
            },
            statusEvent = StatusEvent.Idle
        )

        viewModel.load()

        loading()

        viewModel.participateById(eventId, participatedEvent.participatedByMe)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при участии в событии.
     */
    @Test
    fun `participate in event with error`() {
        val error = RuntimeException("test error participate")

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun participateById(
                    eventId: Long,
                    participatedByMe: Boolean
                ): EventData = throw error
            }
        )

        val expected = EventState(
            events = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.participateById(1, false)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки корректного удаления события.
     */
    @Test
    fun `delete event successfully`() {
        val eventId = 1L

        val initialEvents = listOf(
            EventData(id = eventId, content = "Event 1"),
            EventData(id = 2, content = "Event 2")
        )

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun getEvents(): List<EventData> = initialEvents
                override suspend fun deleteById(eventId: Long) {}
            }
        )

        val expectedEvents = initialEvents.filter { event: EventData ->
            event.id != eventId
        }
            .map { event: EventData ->
                mapper.map(event)
            }

        val expected = EventState(
            events = expectedEvents,
            statusEvent = StatusEvent.Idle
        )

        viewModel.load()

        loading()

        viewModel.deleteById(eventId)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }

    /**
     * Тест для проверки обработки ошибки при удалении события.
     */
    @Test
    fun `delete event with error`() {
        val error = RuntimeException("test error delete")

        val viewModel = EventViewModel(
            object : TestEventRepository {
                override suspend fun deleteById(eventId: Long) = throw error
            }
        )

        val expected = EventState(
            events = null,
            statusEvent = StatusEvent.Error(error)
        )

        viewModel.deleteById(1)

        val actual = viewModel.state.value

        assertEquals(expected, actual)
    }
}

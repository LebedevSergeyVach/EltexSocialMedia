package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository

import io.reactivex.rxjava3.core.Single

import org.junit.Assert.assertEquals
import org.junit.Test

*
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
 * @see EventRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestSchedulersProvider Провайдер планировщиков для синхронного выполнения тестов.


class NewEventViewModelTest {

*
     * Тест для проверки успешного сохранения нового события.
     *
     * @param content Содержимое нового события.
     * @return Успешное состояние с сохраненным событием.


    @Test
    fun `save new event successfully`() {
        val content = "New Event Content"
        val newEvent = EventData(id = 1, content = content)

        val viewModel = NewEventViewModel(
            repository = object : EventRepository {
                override fun save(eventId: Long, content: String, link: String, option: String, data: String): Single<EventData> =
                    Single.just(newEvent)
            },
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content, "", "", "")

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(newEvent, viewModel.state.value.event)
    }

*
     * Тест для проверки обработки ошибки при сохранении нового события.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `save new event with error`() {
        val error = RuntimeException("test error save")
        val content = "New Event Content"

        val viewModel = NewEventViewModel(
            repository = object : EventRepository {
                override fun save(eventId: Long, content: String, link: String, option: String, data: String): Single<EventData> =
                    Single.error(error)
            },
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content, "", "", "")

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }

*
     * Тест для проверки успешного обновления существующего события.
     *
     * @param eventId Идентификатор существующего события.
     * @param content Новое содержимое события.
     * @return Успешное состояние с обновленным событием.


    @Test
    fun `update existing event successfully`() {
        val eventId = 1L
        val content = "Updated Event Content"
        val updatedEvent = EventData(id = eventId, content = content)

        val viewModel = NewEventViewModel(
            repository = object : EventRepository {
                override fun save(eventId: Long, content: String, link: String, option: String, data: String): Single<EventData> =
                    Single.just(updatedEvent)
            },
            eventId = eventId,
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content, "", "", "")

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(updatedEvent, viewModel.state.value.event)
    }

*
     * Тест для проверки обработки ошибки при обновлении существующего события.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `update existing event with error`() {
        val error = RuntimeException("test error update")
        val eventId = 1L
        val content = "Updated Event Content"

        val viewModel = NewEventViewModel(
            repository = object : EventRepository {
                override fun save(eventId: Long, content: String, link: String, option: String, data: String): Single<EventData> =
                    Single.error(error)
            },
            eventId = eventId,
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.save(content, "", "", "")

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }
}

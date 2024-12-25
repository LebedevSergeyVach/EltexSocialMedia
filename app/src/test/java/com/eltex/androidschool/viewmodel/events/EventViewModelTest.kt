package com.eltex.androidschool.viewmodel.events

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.repository.events.EventRepository
import com.eltex.androidschool.ui.events.EventUiModel

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

import org.junit.Assert.assertEquals
import org.junit.Test

*
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
 * @see EventRepository Интерфейс репозитория, используемый для мокирования данных.
 * @see TestSchedulersProvider Провайдер планировщиков для синхронного выполнения тестов.


class EventViewModelTest {

*
     * Тест для проверки корректной загрузки списка событий.
     *
     * @param events Список событий, которые будут возвращены из репозитория.
     * @return Успешное состояние с загруженными событиями.


    @Test
    fun `load events successfully`() {
        val events = listOf(
            EventData(id = 1, content = "Event 1"),
            EventData(id = 2, content = "Event 2")
        )

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun getEvents(): Single<List<EventData>> = Single.just(events)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(events.size, viewModel.state.value.events?.size)
    }

*
     * Тест для проверки обработки ошибки при загрузке списка событий.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `load events with error`() {
        val error = RuntimeException("test error load")

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun getEvents(): Single<List<EventData>> = Single.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }

*
     * Тест для проверки корректного лайка события.
     *
     * @param eventId Идентификатор события, которое нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь это событие.
     * @return Успешное состояние с обновленным списком событий.


    @Test
    fun `like event successfully`() {
        val eventId = 1L
        val initialEvents = listOf(
            EventData(id = eventId, content = "Event 1", likedByMe = false),
            EventData(id = 2, content = "Event 2", likedByMe = false)
        )
        val likedEvent = EventData(id = eventId, content = "Event 1", likedByMe = true)

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun getEvents(): Single<List<EventData>> = Single.just(initialEvents)
                override fun likeById(eventId: Long, likedByMe: Boolean): Single<EventData> =
                    Single.just(likedEvent)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        viewModel.likeById(eventId, false)

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(true, viewModel.state.value.events?.find { event: EventUiModel ->
            event.id == eventId
        }
            ?.likedByMe)
    }

*
     * Тест для проверки обработки ошибки при лайке события.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `like event with error`() {
        val error = RuntimeException("test error like")

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun likeById(eventId: Long, likedByMe: Boolean): Single<EventData> =
                    Single.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.likeById(1, false)

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }

*
     * Тест для проверки корректного участия в событии.
     *
     * @param eventId Идентификатор события, в котором нужно участвовать.
     * @param participatedByMe Флаг, указывающий, участвует ли текущий пользователь в этом событии.
     * @return Успешное состояние с обновленным списком событий.


    @Test
    fun `participate in event successfully`() {
        val eventId = 1L
        val initialEvents = listOf(
            EventData(id = eventId, content = "Event 1", participatedByMe = false),
            EventData(id = 2, content = "Event 2", participatedByMe = false)
        )
        val participatedEvent = EventData(id = eventId, content = "Event 1", participatedByMe = true)

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun getEvents(): Single<List<EventData>> = Single.just(initialEvents)
                override fun participateById(eventId: Long, participatedByMe: Boolean): Single<EventData> =
                    Single.just(participatedEvent)
            },
            TestSchedulersProvider
        )

        viewModel.load()

        viewModel.participateById(eventId, false)

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(true, viewModel.state.value.events?.find { event: EventUiModel ->
            event.id == eventId
        }
            ?.participatedByMe)
    }

*
     * Тест для проверки обработки ошибки при участии в событии.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `participate in event with error`() {
        val error = RuntimeException("test error participate")

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun participateById(eventId: Long, participatedByMe: Boolean): Single<EventData> =
                    Single.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.participateById(1, false)

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }

*
     * Тест для проверки корректного удаления события.
     *
     * @param eventId Идентификатор события, которое нужно удалить.
     * @return Успешное состояние с обновленным списком событий.


    @Test
    fun `delete event successfully`() {
        val eventId = 1L
        val events = listOf(
            EventData(id = eventId, content = "Event 1"),
            EventData(id = 2, content = "Event 2")
        )

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun deleteById(eventId: Long): Completable = Completable.complete()
                override fun getEvents(): Single<List<EventData>> = Single.just(events)
            },
            TestSchedulersProvider
        )

        viewModel.deleteById(eventId)

        assertEquals(StatusEvent.Idle, viewModel.state.value.statusEvent)
        assertEquals(1, viewModel.state.value.events?.size)
        assertEquals(false, viewModel.state.value.events?.any { event: EventUiModel ->
            event.id == eventId
        })
    }

*
     * Тест для проверки обработки ошибки при удалении события.
     *
     * @param error Исключение, которое будет возвращено из репозитория.
     * @return Состояние с ошибкой.


    @Test
    fun `delete event with error`() {
        val error = RuntimeException("test error delete")

        val viewModel = EventViewModel(
            object : EventRepository {
                override fun deleteById(eventId: Long): Completable = Completable.error(error)
            },
            TestSchedulersProvider
        )

        viewModel.deleteById(1)

        assertEquals(StatusEvent.Error(error), viewModel.state.value.statusEvent)
    }
}

package com.eltex.androidschool.viewmodel.events.events

import arrow.core.Either

import com.eltex.androidschool.model.events.EventWithError
import com.eltex.androidschool.ui.events.EventUiModel

/**
 * Запечатанный интерфейс, представляющий сообщения, которые могут быть отправлены в хранилище (Store)
 * для изменения состояния событий. Сообщения используются для обработки различных действий, таких как
 * загрузка событий, лайки, участие, удаление и обработка ошибок.
 */
sealed interface EventMessage {
    /**
     * Сообщение для загрузки следующей страницы событий.
     */
    data object LoadNextPage : EventMessage

    /**
     * Сообщение для обновления списка событий (например, при pull-to-refresh).
     */
    data object Refresh : EventMessage

    /**
     * Сообщение для лайка или снятия лайка с события.
     *
     * @property event Событие, которое нужно лайкнуть или убрать лайк.
     */
    data class Like(val event: EventUiModel) : EventMessage

    /**
     * Сообщение для участия или отмены участия в событии.
     *
     * @property event Событие, в котором нужно участвовать или отменить участие.
     */
    data class Participation(val event: EventUiModel) : EventMessage

    /**
     * Сообщение для удаления события.
     *
     * @property event Событие, которое нужно удалить.
     */
    data class Delete(val event: EventUiModel) : EventMessage

    /**
     * Сообщение для обработки ошибки (например, сброса состояния ошибки).
     */
    data object HandleError : EventMessage

    /**
     * Сообщение, которое возникает при ошибке удаления события.
     *
     * @property error Информация об ошибке, включая событие и исключение.
     */
    data class DeleteError(val error: EventWithError) : EventMessage

    /**
     * Сообщение, которое возникает после попытки лайкнуть событие.
     *
     * @property result Результат операции лайка, который может быть успешным или содержать ошибку.
     */
    data class LikeResult(val result: Either<EventWithError, EventUiModel>) : EventMessage

    /**
     * Сообщение, которое возникает после попытки участия в событии.
     *
     * @property result Результат операции участия, который может быть успешным или содержать ошибку.
     */
    data class ParticipationResult(val result: Either<EventWithError, EventUiModel>) : EventMessage

    /**
     * Сообщение, которое возникает после загрузки начальной страницы событий.
     *
     * @property result Результат загрузки, который может быть успешным или содержать ошибку.
     */
    data class InitialLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage

    /**
     * Сообщение, которое возникает после загрузки следующей страницы событий.
     *
     * @property result Результат загрузки, который может быть успешным или содержать ошибку.
     */
    data class NextPageLoaded(val result: Either<Throwable, List<EventUiModel>>) : EventMessage
}

package com.eltex.androidschool.viewmodel.posts.postswall

import arrow.core.Either

import com.eltex.androidschool.model.posts.PostWithError
import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Запечатанный интерфейс, представляющий сообщения, которые могут быть отправлены в хранилище (Store)
 * для изменения состояния постов на стене пользователя. Сообщения используются для обработки различных действий, таких как
 * загрузка постов, лайки, удаление и обработка ошибок.
 */
sealed interface PostWallMessage {

    /**
     * Сообщение для загрузки следующей страницы постов.
     */
    data object LoadNextPage : PostWallMessage

    /**
     * Сообщение для обновления списка постов (например, при pull-to-refresh).
     */
    data object Refresh : PostWallMessage

    /**
     * Сообщение для лайка или снятия лайка с поста.
     *
     * @property post Пост, который нужно лайкнуть или убрать лайк.
     */
    data class Like(val post: PostUiModel) : PostWallMessage

    /**
     * Сообщение для удаления поста.
     *
     * @property post Пост, который нужно удалить.
     */
    data class Delete(val post: PostUiModel) : PostWallMessage

    /**
     * Сообщение для обработки ошибки (например, сброса состояния ошибки).
     */
    data object HandleError : PostWallMessage

    /**
     * Сообщение, которое возникает при ошибке удаления поста.
     *
     * @property error Информация об ошибке, включая пост и исключение.
     */
    data class DeleteError(val error: PostWithError) : PostWallMessage

    /**
     * Сообщение, которое возникает после попытки лайкнуть пост.
     *
     * @property result Результат операции лайка, который может быть успешным или содержать ошибку.
     */
    data class LikeResult(val result: Either<PostWithError, PostUiModel>) : PostWallMessage

    /**
     * Сообщение, которое возникает после загрузки начальной страницы постов.
     *
     * @property result Результат загрузки, который может быть успешным или содержать ошибку.
     */
    data class InitialLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostWallMessage

    /**
     * Сообщение, которое возникает после загрузки следующей страницы постов.
     *
     * @property result Результат загрузки, который может быть успешным или содержать ошибку.
     */
    data class NextPageLoaded(val result: Either<Throwable, List<PostUiModel>>) : PostWallMessage
}

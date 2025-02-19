package com.eltex.androidschool.viewmodel.posts.postswall

import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.viewmodel.posts.post.PostStatus

/**
 * Класс, представляющий состояние постов на стене пользователя.
 * Этот класс хранит список постов, текущий статус загрузки и информацию об ошибках.
 *
 * @property posts Список постов, отображаемых в UI.
 * @property statusPost Текущий статус загрузки постов.
 * @property singleError Исключение, которое произошло при выполнении операции (например, лайк или удаление).
 */
data class PostWallState(
    val posts: List<PostUiModel> = emptyList(),
    val statusPost: PostStatus = PostStatus.Idle(),
    val singleError: Throwable? = null,
    var userId: Long = 0L,
) {

    /**
     * Флаг, указывающий, произошла ли ошибка при начальной загрузке постов.
     *
     * @return `true`, если статус загрузки — [PostStatus.EmptyError], иначе `false`.
     */
    val isEmptyError: Boolean = statusPost is PostStatus.EmptyError

    /**
     * Флаг, указывающий, выполняется ли в данный момент обновление списка постов.
     *
     * @return `true`, если статус загрузки — [PostStatus.Refreshing], иначе `false`.
     */
    val isRefreshing: Boolean = statusPost == PostStatus.Refreshing

    /**
     * Исключение, которое произошло при начальной загрузке постов.
     *
     * @return Исключение, если статус загрузки — [PostStatus.EmptyError], иначе `null`.
     */
    val emptyError: Throwable? = (statusPost as? PostStatus.EmptyError)?.reason

    /**
     * Флаг, указывающий, выполняется ли в данный момент начальная загрузка постов, и список постов пуст.
     *
     * @return `true`, если статус загрузки — [PostStatus.EmptyLoading], иначе `false`.
     */
    val isEmptyLoading: Boolean = statusPost == PostStatus.EmptyLoading
}

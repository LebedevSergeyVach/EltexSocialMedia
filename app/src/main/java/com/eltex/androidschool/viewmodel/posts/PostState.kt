package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Состояние ViewModel для постов.
 * Содержит список постов, которые будут отображаться в UI.
 *
 * @property posts Список постов, которые будут отображаться в UI. По умолчанию пустой список.
 * @property statusPost Состояние операции. По умолчанию Idle.
 */
data class PostState(
    val posts: List<PostUiModel>? = null,
    val statusPost: StatusPost = StatusPost.Idle,
) {
    /**
     * Флаг, указывающий, что идет обновление списка постов.
     */
    val isRefreshing: Boolean
        get() = statusPost == StatusPost.Loading && posts?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что идет загрузка списка постов.
     */
    val isEmptyLoading: Boolean
        get() = statusPost == StatusPost.Loading && posts.isNullOrEmpty()

    /**
     * Флаг, указывающий, что произошла ошибка при обновлении списка постов.
     */
    val isRefreshError: Boolean
        get() = statusPost is StatusPost.Error && posts?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что произошла ошибка при загрузке списка постов.
     */
    val isEmptyError: Boolean
        get() = statusPost is StatusPost.Error && posts.isNullOrEmpty()
}

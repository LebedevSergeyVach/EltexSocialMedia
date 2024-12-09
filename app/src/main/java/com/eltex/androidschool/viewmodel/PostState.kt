package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.PostData

/**
 * Состояние ViewModel для постов.
 * Содержит список постов, которые будут отображаться в UI.
 *
 * @property posts Список постов, которые будут отображаться в UI. По умолчанию пустой список.
 * @property status Состояние операции. По умолчанию Idle.
 */
data class PostState(
    val posts: List<PostData>? = null,
    val status: Status = Status.Idle,
) {
    /**
     * Флаг, указывающий, что идет обновление списка постов.
     */
    val isRefreshing: Boolean
        get() = status == Status.Loading && posts?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что идет загрузка списка постов.
     */
    val isEmptyLoading: Boolean
        get() = status == Status.Loading && posts.isNullOrEmpty()

    /**
     * Флаг, указывающий, что произошла ошибка при обновлении списка постов.
     */
    val isRefreshError: Boolean
        get() = status is Status.Error && posts?.isNotEmpty() == true

    /**
     * Флаг, указывающий, что произошла ошибка при загрузке списка постов.
     */
    val isEmptyError: Boolean
        get() = status is Status.Error && posts.isNullOrEmpty()
}

package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.data.posts.PostData

/**
 * Состояние ViewModel для постов.
 * Содержит список постов, которые будут отображаться в UI.
 *
 * @property posts Список постов, которые будут отображаться в UI. По умолчанию пустой список.
 *
 * @sample [PostViewModel] Пример использования состояния в PostViewModel.
 */
data class PostState(
    val posts: List<PostData> = emptyList(),
)

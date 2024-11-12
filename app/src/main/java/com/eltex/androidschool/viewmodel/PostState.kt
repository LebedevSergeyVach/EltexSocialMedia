package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Post


/**
 * Состояние ViewModel для постов.
 * Содержит список постов, которые будут отображаться в UI.
 *
 * @property posts Список постов, которые будут отображаться в UI. По умолчанию пустой список.
 *
 * @sample [PostViewModel] Пример использования состояния в PostViewModel.
 */
data class PostState(
    val posts: List<Post> = emptyList(),
)

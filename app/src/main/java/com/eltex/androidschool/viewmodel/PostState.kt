package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Post


/**
 * Модель для хранения состояния поста
 */
data class PostState(
    val post: Post = Post(),
)

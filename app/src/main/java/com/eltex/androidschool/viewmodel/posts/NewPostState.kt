package com.eltex.androidschool.viewmodel.posts

import com.eltex.androidschool.data.posts.PostData

/**
 * Состояние ViewModel для создания или обновления поста.
 *
 * @property post Обновленный или созданный пост. По умолчанию null.
 * @property statusPost Состояние операции. По умолчанию Idle.
 */
data class NewPostState(
    val post: PostData? = null,
    val statusPost: StatusPost = StatusPost.Idle,
)

package com.eltex.androidschool.viewmodel.posts.newposts

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние ViewModel для создания или обновления поста.
 *
 * @property post Обновленный или созданный пост. По умолчанию null.
 * @property statusPost Состояние операции. По умолчанию Idle.
 */
data class NewPostState(
    val post: PostData? = null,
    val statusPost: StatusLoad = StatusLoad.Idle,
)

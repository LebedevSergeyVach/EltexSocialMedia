package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.PostData

/**
 * Состояние ViewModel для создания или обновления поста.
 *
 * @property post Обновленный или созданный пост. По умолчанию null.
 * @property status Состояние операции. По умолчанию Idle.
 */
data class NewPostState(
    val post: PostData? = null,
    val status: Status = Status.Idle,
)

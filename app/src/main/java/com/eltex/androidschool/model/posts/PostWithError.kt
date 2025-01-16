package com.eltex.androidschool.model.posts

import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Класс, представляющий пост с ошибкой. Используется для хранения информации о посте,
 * который вызвал ошибку, и самой ошибки.
 *
 * @property post Пост, связанный с ошибкой.
 * @property throwable Ошибка, которая произошла при выполнении операции с постом.
 */
data class PostWithError(
    val post: PostUiModel,
    val throwable: Throwable,
)

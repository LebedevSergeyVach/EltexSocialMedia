package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.data.Post


/**
 * Класс [PostState] представляет состояние Поста
 * Информация о Посте, которая используется в ViewModel для обновления UI
 *
 * @property post Данные о Посте
 */
data class PostState(
    val post: Post = Post(),
)

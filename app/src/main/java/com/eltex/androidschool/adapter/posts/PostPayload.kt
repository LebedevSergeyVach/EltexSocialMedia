package com.eltex.androidschool.adapter.posts

/**
 * Класс, представляющий изменения в посте.
 * Используется для передачи изменений в элемент списка, чтобы избежать полного обновления ViewHolder.
 *
 * @property likeByMe Состояние лайка (лайкнут/не лайкнут).
 * @property likes Количество лайков у поста.
 *
 * @see PostItemCallback Используется для передачи изменений в элемент списка.
 */
data class PostPayload (
    val likeByMe: Boolean? = null,
    val likes: Int? = null,
) {

    /**
     * Проверяет, есть ли изменения в объекте.
     *
     * @return Boolean true, если есть изменения, иначе false.
     */
    fun isNotEmpty(): Boolean = likeByMe != null || likes != null
}

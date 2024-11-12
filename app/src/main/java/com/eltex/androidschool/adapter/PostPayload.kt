package com.eltex.androidschool.adapter

/**
 * Класс, представляющий изменения в посте.
 *
 * @property likeByMe Состояние лайка (лайкнут/не лайкнут).
 *
 * @see PostItemCallback Используется для передачи изменений в элемент списка.
 */
data class PostPayload (
    val likeByMe: Boolean? = null,
) {
    /**
     * Проверяет, есть ли изменения в объекте.
     *
     * @return Boolean true, если есть изменения, иначе false.
     */
    fun isNotEmpty(): Boolean = likeByMe != null
}

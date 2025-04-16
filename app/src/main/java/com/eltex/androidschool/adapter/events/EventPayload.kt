package com.eltex.androidschool.adapter.events

/**
 * Класс, представляющий изменения в событии.
 *
 * @property likeByMe Состояние лайка (лайкнут/не лайкнут).
 * @property likes Количество лайков у события.
 * @property participateByMe Состояние участия (участвует/не участвует).
 * @property participates Количество участников у события.
 *
 * @see EventItemCallback Используется для передачи изменений в элемент списка.
 */
data class EventPayload(
    val likeByMe: Boolean? = null,
    val likes: Int? = null,
    val participateByMe: Boolean? = null,
    val participates: Int? = null,
) {

    /**
     * Проверяет, есть ли изменения в объекте.
     *
     * @return Boolean true, если есть изменения, иначе false.
     */
    fun isNotEmpty(): Boolean =
        likeByMe != null || likes != null || participateByMe != null || participates != null
}

package com.eltex.androidschool.adapter.events

/**
 * Класс, представляющий изменения в событии.
 *
 * @property likeByMe Состояние лайка (лайкнут/не лайкнут).
 * @property participateByMe Состояние участия (участвует/не участвует).
 *
 * @see EventItemCallback Используется для передачи изменений в элемент списка.
 */
data class EventPayload(
    val likeByMe: Boolean? = null,
    val participateByMe: Boolean? = null
) {
    /**
     * Проверяет, есть ли изменения в объекте.
     *
     * @return Boolean true, если есть изменения, иначе false.
     */
    fun isNotEmpty(): Boolean = likeByMe != null || participateByMe != null
}

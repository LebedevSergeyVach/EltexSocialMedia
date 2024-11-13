package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.data.Event

/**
 * Callback для сравнения элементов списка событий.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class EventItemCallback : DiffUtil.ItemCallback<Event>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: Event, newItem: Event): Any? =
        EventPayload(
            likeByMe = newItem.likeByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likeByMe
            },
            participateByMe = newItem.participateByMe.takeIf { participateByMe: Boolean ->
                participateByMe != oldItem.participateByMe
            }
        )
            .takeIf { eventPayload: EventPayload ->
                eventPayload.isNotEmpty()
            }
}

package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.data.EventData

/**
 * Callback для сравнения элементов списка событий.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class EventItemCallback : DiffUtil.ItemCallback<EventData>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: EventData, newItem: EventData): Boolean = oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: EventData, newItem: EventData): Boolean = oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: EventData, newItem: EventData): Any? =
        EventPayload(
            likeByMe = newItem.likedByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likedByMe
            },
            participateByMe = newItem.participatedByMe.takeIf { participateByMe: Boolean ->
                participateByMe != oldItem.participatedByMe
            }
        )
            .takeIf { eventPayload: EventPayload ->
                eventPayload.isNotEmpty()
            }
}

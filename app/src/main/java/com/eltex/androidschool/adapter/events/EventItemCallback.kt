package com.eltex.androidschool.adapter.events

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.events.EventUiModel

/**
 * Callback для сравнения элементов списка событий.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class EventItemCallback : DiffUtil.ItemCallback<EventUiModel>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
        oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: EventUiModel, newItem: EventUiModel): Boolean =
        oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: EventUiModel, newItem: EventUiModel): Any? =
        EventPayload(
            likeByMe = newItem.likedByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likedByMe
            },
            likes = newItem.likes.takeIf { likes: Int ->
                likes != oldItem.likes
            },
            participateByMe = newItem.participatedByMe.takeIf { participateByMe: Boolean ->
                participateByMe != oldItem.participatedByMe
            },
            participates = newItem.participates.takeIf { participates: Int ->
                participates != oldItem.participates
            }
        )
            .takeIf { eventPayload: EventPayload ->
                eventPayload.isNotEmpty()
            }
}

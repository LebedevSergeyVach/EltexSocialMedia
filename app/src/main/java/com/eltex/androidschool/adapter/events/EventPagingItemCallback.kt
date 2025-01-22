package com.eltex.androidschool.adapter.events

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.events.EventPagingModel

/**
 * Callback для сравнения элементов списка событий с поддержкой пагинации.
 * Используется для определения изменений в списке событий и оптимизации обновлений RecyclerView.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class EventPagingItemCallback : DiffUtil.ItemCallback<EventPagingModel>() {

    /**
     * Делегат для сравнения элементов списка событий.
     * Используется для обработки сравнения данных внутри элементов списка.
     *
     * @see EventItemCallback Callback для сравнения элементов списка событий.
     */
    private val delegate = EventItemCallback()

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: EventPagingModel, newItem: EventPagingModel): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: EventPagingModel, newItem: EventPagingModel): Boolean =
        oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: EventPagingModel, newItem: EventPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return false
        }

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}

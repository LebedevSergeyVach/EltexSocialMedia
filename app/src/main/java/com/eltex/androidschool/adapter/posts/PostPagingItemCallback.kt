package com.eltex.androidschool.adapter.posts

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.posts.PostPagingModel

/**
 * Callback для сравнения элементов списка постов с поддержкой пагинации.
 * Используется для определения изменений в списке постов и оптимизации обновлений RecyclerView.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class PostPagingItemCallback : DiffUtil.ItemCallback<PostPagingModel>() {

    /**
     * Делегат для сравнения элементов списка постов.
     * Используется для обработки сравнения данных внутри элементов списка.
     *
     * @see PostItemCallback Callback для сравнения элементов списка постов.
     */
    private val delegate = PostItemCallback()

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean {
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
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean =
        oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: PostPagingModel, newItem: PostPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}

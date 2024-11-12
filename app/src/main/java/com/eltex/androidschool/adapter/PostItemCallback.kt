package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.data.Post

/**
 * Callback для сравнения элементов списка постов.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class PostItemCallback : DiffUtil.ItemCallback<Post>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: Post, newItem: Post): Any? =
        PostPayload(
            likeByMe = newItem.likeByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likeByMe
            }
        )
            .takeIf { postPayload: PostPayload ->
                postPayload.isNotEmpty()
            }
}

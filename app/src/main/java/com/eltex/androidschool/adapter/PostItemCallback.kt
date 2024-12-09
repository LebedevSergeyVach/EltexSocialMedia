package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.data.PostData

/**
 * Callback для сравнения элементов списка постов.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class PostItemCallback : DiffUtil.ItemCallback<PostData>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean = oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean = oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: PostData, newItem: PostData): Any? =
        PostPayload(
            likeByMe = newItem.likedByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likedByMe
            }
        )
            .takeIf { postPayload: PostPayload ->
                postPayload.isNotEmpty()
            }
}

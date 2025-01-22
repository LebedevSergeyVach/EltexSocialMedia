package com.eltex.androidschool.adapter.posts

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.posts.PostUiModel

/**
 * Callback для сравнения элементов списка постов.
 * Используется для определения изменений в списке постов и оптимизации обновлений RecyclerView.
 *
 * @see DiffUtil.ItemCallback Базовый класс для сравнения элементов списка.
 */
class PostItemCallback : DiffUtil.ItemCallback<PostUiModel>() {

    /**
     * Проверяет, являются ли элементы одним и тем же объектом.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если элементы одинаковы, иначе false.
     */
    override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem.id == newItem.id

    /**
     * Проверяет, содержат ли элементы одинаковые данные.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Boolean true, если данные элементов одинаковы, иначе false.
     */
    override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean =
        oldItem == newItem

    /**
     * Возвращает объект, содержащий изменения в элементе.
     *
     * @param oldItem Старый элемент.
     * @param newItem Новый элемент.
     *
     * @return Any? Объект, содержащий изменения, или null, если изменений нет.
     */
    override fun getChangePayload(oldItem: PostUiModel, newItem: PostUiModel): Any? =
        PostPayload(
            likeByMe = newItem.likedByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likedByMe
            },
            likes = newItem.likes.takeIf { likes: Int ->
                likes != oldItem.likes
            }
        )
            .takeIf { postPayload: PostPayload ->
                postPayload.isNotEmpty()
            }
}

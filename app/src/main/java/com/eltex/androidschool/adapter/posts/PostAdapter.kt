package com.eltex.androidschool.adapter.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.CardPostBinding

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.utils.singleVibration

/**
 * Адаптер для отображения списка постов в RecyclerView.
 *
 * Этот класс отвечает за управление списком постов и их отображение в RecyclerView.
 * Он также обрабатывает события, такие как клики на кнопки "лайк", "поделиться" и "удалить".
 *
 * @param listener Слушатель событий, который будет вызываться при кликах на элементы списка.
 *
 * @see PostViewHolder ViewHolder, используемый для отображения элементов списка.
 * @see PostItemCallback Callback для сравнения элементов списка.
 */
class PostAdapter(
    private val listener: PostListener,
    private val context: Context
) : ListAdapter<PostData, PostViewHolder>(PostItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с постами.
     */
    interface PostListener {
        fun onLikeClicked(post: PostData)
        fun onShareClicked(post: PostData)
        fun onDeleteClicked(post: PostData)
        fun onUpdateClicked(post: PostData)
    }

    /**
     * Создает новый ViewHolder для отображения элемента списка.
     *
     * @param parent Родительский ViewGroup.
     * @param viewType Тип View.
     *
     * @return PostViewHolder Новый ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(layoutInflater, parent, false)

        val viewHolder = PostViewHolder(binding, parent.context)

        binding.like.setOnClickListener {
            listener.onLikeClicked(getItem(viewHolder.adapterPosition))
        }

        binding.share.setOnClickListener {
            listener.onShareClicked(getItem(viewHolder.adapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            PopupMenu(view.context, view).apply {
                inflate(R.menu.menu_post)

                setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.delete_post -> {
                            listener.onDeleteClicked(getItem(viewHolder.adapterPosition))
                            context.singleVibration(75L)
                            true
                        }

                        R.id.update_post -> {
                            listener.onUpdateClicked(getItem(viewHolder.adapterPosition))
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }

                show()
            }
        }

        return viewHolder
    }

    /**
     * Привязывает данные к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindPost(getItem(position))
    }

    /**
     * Привязывает данные к существующему ViewHolder с учетом изменений.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     * @param payloads Список изменений.
     */
    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { post ->
                if (post is PostPayload) {
                    holder.bind(post)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }
}

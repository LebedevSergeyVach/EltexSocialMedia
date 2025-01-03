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
import com.eltex.androidschool.ui.posts.PostUiModel
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
    private val context: Context,
    private val currentUserId: Long
) : ListAdapter<PostUiModel, PostViewHolder>(PostItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с постами.
     */
    interface PostListener {
        fun onLikeClicked(post: PostUiModel)
        fun onShareClicked(post: PostUiModel)
        fun onDeleteClicked(post: PostUiModel)
        fun onUpdateClicked(post: PostUiModel)
        fun onGetUserClicked(post: PostUiModel)
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

        binding.avatar.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.adapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            showPopupMenu(view, viewHolder.adapterPosition)
        }

        binding.cardPost.setOnLongClickListener { view: View ->
            showPopupMenu(view, viewHolder.adapterPosition)
            true
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
        holder.bindPost(getItem(position), currentUserId)
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

    /**
     * Показывает PopupMenu с действиями для поста.
     *
     * @param view View, к которому привязано меню.
     * @param position Позиция поста в списке.
     */
    private fun showPopupMenu(view: View, position: Int) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_post)

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.delete_post -> {
                        listener.onDeleteClicked(getItem(position))
                        context.singleVibration(75L)
                        true
                    }

                    R.id.update_post -> {
                        listener.onUpdateClicked(getItem(position))
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
}

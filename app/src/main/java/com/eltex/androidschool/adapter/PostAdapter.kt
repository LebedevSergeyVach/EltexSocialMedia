package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.data.Post

/**
 * Адаптер для отображения списка постов в RecyclerView.
 *
 * @param likeClickListener Функция, которая будет вызываться при клике на лайк.
 * @param shareClickListener Функция, которая будет вызываться при клике на поделиться.
 *
 * @see PostViewHolder ViewHolder, используемый для отображения элементов списка.
 * @see PostItemCallback Callback для сравнения элементов списка.
 */
class PostAdapter(
    private val listener: PostListener,
) : ListAdapter<Post, PostViewHolder>(PostItemCallback()) {

    interface PostListener {
        fun onLikeClicled(post: Post)
        fun onShareClicked(post: Post)
        fun onDeleteClicked(post: Post)
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
            listener.onLikeClicled(getItem(viewHolder.adapterPosition))
        }

        binding.share.setOnClickListener {
            listener.onShareClicked(getItem(viewHolder.adapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            PopupMenu(view.context, view).apply {
                inflate(R.menu.post_menu)

                setOnMenuItemClickListener { menuItem: MenuItem ->
                    if (menuItem.itemId == R.id.delete_post) {
                        listener.onDeleteClicked(getItem(viewHolder.adapterPosition))
                        true
                    } else {
                        false
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

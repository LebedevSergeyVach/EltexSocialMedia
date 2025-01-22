package com.eltex.androidschool.adapter.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.common.ErrorViewHolder
import com.eltex.androidschool.adapter.common.LoadingViewHolder
import com.eltex.androidschool.adapter.common.SkeletonViewHolder
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.ItemProgressBinding
import com.eltex.androidschool.databinding.ItemSkeletonPostBinding
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.posts.PostPagingModel
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck

/**
 * Адаптер для отображения списка постов в RecyclerView.
 *
 * Этот класс отвечает за управление списком постов и их отображение в RecyclerView.
 * Он также обрабатывает события, такие как клики на кнопки "лайк", "поделиться" и "удалить".
 *
 * @param listener Слушатель событий, который будет вызываться при кликах на элементы списка.
 * @param context Контекст приложения.
 * @param currentUserId ID текущего пользователя для определения прав на редактирование постов.
 *
 * @see PostViewHolder ViewHolder, используемый для отображения элементов списка.
 * @see PostItemCallback Callback для сравнения элементов списка.
 */
class PostAdapter(
    private val listener: PostListener,
    private val context: Context,
    private val currentUserId: Long
) : ListAdapter<PostPagingModel, RecyclerView.ViewHolder>(PostPagingItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с постами.
     */
    interface PostListener {
        /**
         * Вызывается при клике на кнопку "лайк".
         *
         * @param post Пост, который был лайкнут или дизлайкнут.
         */
        fun onLikeClicked(post: PostUiModel)

        /**
         * Вызывается при клике на кнопку "поделиться".
         *
         * @param post Пост, которым поделились.
         */
        fun onShareClicked(post: PostUiModel)

        /**
         * Вызывается при клике на кнопку "удалить".
         *
         * @param post Пост, который нужно удалить.
         */
        fun onDeleteClicked(post: PostUiModel)

        /**
         * Вызывается при клике на кнопку "обновить".
         *
         * @param post Пост, который нужно обновить.
         */
        fun onUpdateClicked(post: PostUiModel)

        /**
         * Вызывается при клике на автора поста.
         *
         * @param post Пост, автор которого был выбран.
         */
        fun onGetUserClicked(post: PostUiModel)
    }

    /**
     * Возвращает тип View для элемента списка на основе его позиции.
     *
     * @param position Позиция элемента в списке.
     * @return Int Идентификатор макета для элемента списка.
     */
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PagingModel.Data -> R.layout.card_post
            is PagingModel.Error -> R.layout.item_error
            PagingModel.Loading -> R.layout.item_skeleton_post
        }

    /**
     * Создает новый ViewHolder для отображения элемента списка.
     *
     * @param parent Родительский ViewGroup.
     * @param viewType Тип View.
     *
     * @return RecyclerView.ViewHolder Новый ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when (viewType) {
            R.layout.card_post -> createPostViewHolder(parent = parent)
            R.layout.item_error -> createItemErrorViewHolder(parent = parent)
            R.layout.item_skeleton_post -> createItemSkeletonViewHolder(parent = parent)
            else -> error("PostAdapter.onCreateViewHolder: Unknown viewType $viewType")
        }

        return viewHolder
    }

    /**
     * Привязывает данные к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as PostViewHolder).bindPost(
                post = item.value,
                currentUserId = currentUserId
            )

            is PagingModel.Error -> (holder as ErrorViewHolder).bind(
                error = item.reason
            )

            is PagingModel.Loading -> (holder as SkeletonViewHolder).bind()
        }
    }

    /**
     * Привязывает данные к существующему ViewHolder с учетом изменений.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     * @param payloads Список изменений.
     */
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { post ->
                if (post is PostPayload) {
                    (holder as? PostViewHolder)?.bind(payload = post)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    /**
     * Создает ViewHolder для отображения поста.
     *
     * @param parent Родительский ViewGroup.
     * @return PostViewHolder Новый ViewHolder для поста.
     */
    private fun createPostViewHolder(
        parent: ViewGroup
    ): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(layoutInflater, parent, false)

        val viewHolder = PostViewHolder(binding = binding, context = parent.context)

        binding.like.setOnClickListener {
            val item: PagingModel.Data<PostUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onLikeClicked)
        }

        binding.share.setOnClickListener {
            val item: PagingModel.Data<PostUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onShareClicked)
        }

        binding.avatar.setOnClickListener {
            val item: PagingModel.Data<PostUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onGetUserClicked)
        }

        binding.author.setOnClickListener {
            val item: PagingModel.Data<PostUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onGetUserClicked)
        }

        binding.menu.setOnClickListener { view: View ->
            showPopupMenu(view = view, position = viewHolder.bindingAdapterPosition)
        }

        return viewHolder
    }

    /**
     * Создает ViewHolder для отображения состояния загрузки.
     *
     * @param parent Родительский ViewGroup.
     * @return LoadingViewHolder Новый ViewHolder для состояния загрузки.
     */
    private fun createItemProgressLoadingViewHolder(parent: ViewGroup): LoadingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProgressBinding.inflate(layoutInflater, parent, false)

        return LoadingViewHolder(binding = binding)
    }

    /**
     * Создает ViewHolder для отображения состояния ошибки.
     *
     * @param parent Родительский ViewGroup.
     * @return ErrorViewHolder Новый ViewHolder для состояния ошибки.
     */
    private fun createItemErrorViewHolder(parent: ViewGroup): ErrorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemErrorBinding.inflate(layoutInflater, parent, false)

        return ErrorViewHolder(binding = binding)
    }

    /**
     * Создает ViewHolder для отображения загрузки в виде анимации скелетона карточки поста.
     *
     * @param parent Родительский ViewGroup.
     * @return SkeletonViewHolder Новый ViewHolder для состояния загрузки.
     */
    private fun createItemSkeletonViewHolder(parent: ViewGroup): SkeletonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSkeletonPostBinding.inflate(layoutInflater, parent, false)

        return SkeletonViewHolder(binding = binding)
    }

    /**
     * Показывает PopupMenu с действиями для поста.
     *
     * @param view View, к которому привязано меню.
     * @param position Позиция поста в списке.
     */
    private fun showPopupMenu(view: View, position: Int) {
        context.singleVibrationWithSystemCheck(35)

        PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_post)

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.delete_post -> {
                        val item: PagingModel.Data<PostUiModel>? =
                            getItem(position) as? PagingModel.Data

                        item?.value?.let(listener::onDeleteClicked)
                        context.singleVibrationWithSystemCheck(35)
                        true
                    }

                    R.id.update_post -> {
                        val item: PagingModel.Data<PostUiModel>? =
                            getItem(position) as? PagingModel.Data

                        item?.value?.let(listener::onUpdateClicked)
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

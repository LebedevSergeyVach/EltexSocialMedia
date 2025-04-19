package com.eltex.androidschool.adapter.posts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder

import androidx.appcompat.widget.PopupMenu

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.common.DateSeparatorViewHolder
import com.eltex.androidschool.adapter.common.ErrorViewHolder
import com.eltex.androidschool.adapter.common.LoadingViewHolder
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.databinding.ItemDateSeparatorBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.ItemProgressBinding
import com.eltex.androidschool.databinding.ItemSkeletonPostBinding
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.posts.PostPagingModel
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck

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

    private companion object {
        private const val TYPE_POST = 0
        private const val TYPE_ERROR = 1
        private const val TYPE_LOADING = 2
        private const val TYPE_DATE_SEPARATOR = 3
    }

    /**
     * Интерфейс для обработки событий, связанных с постами.
     */
    interface PostListener {
        fun onLikeClicked(post: PostUiModel)
        fun onShareClicked(post: PostUiModel)
        fun onDeleteClicked(post: PostUiModel)
        fun onUpdateClicked(post: PostUiModel)
        fun onGetUserClicked(post: PostUiModel)
        fun onCommentsClicked(post: PostUiModel)
        fun onGetPostDetailsClicked(post: PostUiModel)
        fun onRetryPageClicked()
    }

    /**
     * Возвращает тип View для элемента списка на основе его позиции.
     *
     * @param position Позиция элемента в списке.
     * @return Int Идентификатор макета для элемента списка.
     */
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PagingModel.Data -> TYPE_POST
            is PagingModel.Error -> TYPE_ERROR
            is PagingModel.Loading -> TYPE_LOADING
            is PagingModel.DateSeparator -> TYPE_DATE_SEPARATOR
        }

    /**
     * Создает новый ViewHolder для отображения элемента списка.
     *
     * @param parent Родительский ViewGroup.
     * @param viewType Тип View.
     *
     * @return RecyclerView.ViewHolder Новый ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_POST -> createPostViewHolder(parent = parent)
            TYPE_ERROR -> createItemErrorViewHolder(parent = parent)
            TYPE_LOADING -> createItemSkeletonViewHolder(parent = parent)
            TYPE_DATE_SEPARATOR -> createItemDateSeparatorViewHolder(parent = parent)
            else -> error("PostAdapter.onCreateViewHolder: Unknown viewType $viewType")
        }

    /**
     * Привязывает данные к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item: PostPagingModel = getItem(position)) {
            is PagingModel.Data -> (holder as PostViewHolder).bindPost(
                post = item.value,
                currentUserId = currentUserId
            )

            is PagingModel.Error -> (holder as ErrorViewHolder).bind(
                error = item.reason
            )

            is PagingModel.Loading -> (holder as SkeletonPostViewHolder).bind()

            is PagingModel.DateSeparator -> (holder as DateSeparatorViewHolder).bind(item.date)
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
    private fun createPostViewHolder(parent: ViewGroup): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(layoutInflater, parent, false)

        val viewHolder = PostViewHolder(
            binding = binding,
            context = parent.context,
            listener = listener,
        )

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

        binding.comments.setOnClickListener {
            val item: PagingModel.Data<PostUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onCommentsClicked)
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

        binding.retryButtonItem.setOnClickListener {
            listener.onRetryPageClicked()
        }

        return ErrorViewHolder(binding = binding)
    }

    /**
     * Создает ViewHolder для отображения загрузки в виде анимации скелетона карточки поста.
     *
     * @param parent Родительский ViewGroup.
     * @return SkeletonViewHolder Новый ViewHolder для состояния загрузки.
     */
    private fun createItemSkeletonViewHolder(parent: ViewGroup): SkeletonPostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSkeletonPostBinding.inflate(layoutInflater, parent, false)

        return SkeletonPostViewHolder(binding = binding)
    }

    private fun createItemDateSeparatorViewHolder(parent: ViewGroup): DateSeparatorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDateSeparatorBinding.inflate(layoutInflater, parent, false)

        return DateSeparatorViewHolder(binding = binding)
    }

    /**
     * Показывает PopupMenu с действиями для поста.
     *
     * @param view View, к которому привязано меню.
     * @param position Позиция поста в списке.
     */
    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showPopupMenu(view: View, position: Int) {
        context.singleVibrationWithSystemCheck(35)

        val resources = view.context.resources

        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        ).toInt()

        val popup = PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_post)

            if (menu is MenuBuilder) {
                val menuBuilder = menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)

                for (item in menuBuilder.visibleItems) {
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                        }
                    }
                }
            }

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
        }

        popup.show()
    }

    fun getPublicItem(position: Int): PostPagingModel? =
        if (position in 0 until itemCount) getItem(position) else null
}

package com.eltex.androidschool.adapter.events

import android.content.Context

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.PopupMenu

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.common.DateSeparatorViewHolder
import com.eltex.androidschool.adapter.common.ErrorViewHolder
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.databinding.ItemDateSeparatorBinding
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.ItemSkeletonEventBinding
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.events.EventPagingModel
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck

/**
 * Адаптер для отображения списка событий в RecyclerView с поддержкой различных типов элементов.
 * Этот класс отвечает за управление списком событий и их отображение в RecyclerView.
 * Также обрабатывает события, такие как клики на кнопки "лайк", "поделиться", "участвовать" и "удалить".
 *
 * @param listener Слушатель событий, который будет вызываться при кликах на элементы списка.
 * @param context Контекст приложения.
 * @param currentUserId ID текущего пользователя для определения прав на редактирование событий.
 */
class EventAdapterDifferentTypesView(
    private val listener: EventListener,
    private val context: Context,
    private val currentUserId: Long
) : ListAdapter<EventPagingModel, RecyclerView.ViewHolder>(EventPagingItemCallback()) {

    private companion object {
        private const val TYPE_EVENT = 0
        private const val TYPE_ERROR = 1
        private const val TYPE_LOADING = 2
        private const val TYPE_DATE_SEPARATOR = 3
    }

    /**
     * Интерфейс для обработки событий, связанных с событиями.
     */
    interface EventListener {
        fun onLikeClicked(event: EventUiModel)
        fun onShareClicked(event: EventUiModel)
        fun onParticipateClicked(event: EventUiModel)
        fun onDeleteClicked(event: EventUiModel)
        fun onUpdateClicked(event: EventUiModel)
        fun onGetUserClicked(event: EventUiModel)
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
            is PagingModel.Data -> TYPE_EVENT
            is PagingModel.Error -> TYPE_ERROR
            is PagingModel.Loading -> TYPE_LOADING
            is PagingModel.DateSeparator -> TYPE_DATE_SEPARATOR
        }

    /**
     * Создает новый ViewHolder для отображения элемента списка.
     *
     * @param parent Родительский ViewGroup.
     * @param viewType Тип View.
     * @return RecyclerView.ViewHolder Новый ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_EVENT -> createEventViewHolder(parent = parent)
            TYPE_ERROR-> createItemErrorViewHolder(parent = parent)
            TYPE_LOADING -> createItemSkeletonViewHolder(parent = parent)
            TYPE_DATE_SEPARATOR -> createItemDateSeparatorViewHolder(parent = parent)
            else -> error("EventAdapterDifferentTypesView.onCreateViewHolder: Unknown viewType $viewType")
        }


    /**
     * Привязывает данные к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as EventViewHolder).bindEvent(
                event = item.value,
                currentUserId = currentUserId
            )

            is PagingModel.Error -> (holder as ErrorViewHolder).bind(
                error = item.reason
            )

            is PagingModel.Loading -> (holder as SkeletonEventViewHolder).bind()

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
            payloads.forEach { event ->
                if (event is EventPayload) {
                    (holder as? EventViewHolder)?.bind(payload = event)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    /**
     * Создает ViewHolder для отображения события.
     *
     * @param parent Родительский ViewGroup.
     * @return EventViewHolder Новый ViewHolder для события.
     */
    private fun createEventViewHolder(parent: ViewGroup): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(layoutInflater, parent, false)

        val viewHolder = EventViewHolder(
            binding = binding,
            context = parent.context
        )

        binding.like.setOnClickListener {
            val item: PagingModel.Data<EventUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onLikeClicked)
        }

        binding.participate.setOnClickListener {
            val item: PagingModel.Data<EventUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onParticipateClicked)
        }

        binding.share.setOnClickListener {
            val item: PagingModel.Data<EventUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onShareClicked)
        }

        binding.avatar.setOnClickListener {
            val item: PagingModel.Data<EventUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onGetUserClicked)
        }

        binding.author.setOnClickListener {
            val item: PagingModel.Data<EventUiModel>? =
                getItem(viewHolder.bindingAdapterPosition) as? PagingModel.Data

            item?.value?.let(listener::onGetUserClicked)
        }

        binding.menu.setOnClickListener { view: View ->
            showPopupMenu(view = view, position = viewHolder.bindingAdapterPosition)
        }

        return viewHolder
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
     * Создает ViewHolder для отображения скелетона события.
     *
     * @param parent Родительский ViewGroup.
     * @return SkeletonEventViewHolder Новый ViewHolder для скелетона события.
     */
    private fun createItemSkeletonViewHolder(parent: ViewGroup): SkeletonEventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSkeletonEventBinding.inflate(layoutInflater, parent, false)

        return SkeletonEventViewHolder(binding = binding)
    }

    private fun createItemDateSeparatorViewHolder(parent: ViewGroup): DateSeparatorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDateSeparatorBinding.inflate(layoutInflater, parent, false)

        return DateSeparatorViewHolder(binding = binding)
    }


    /**
     * Показывает PopupMenu с действиями для события.
     *
     * @param view View, к которому привязано меню.
     * @param position Позиция события в списке.
     */
    private fun showPopupMenu(view: View, position: Int) {
        context.singleVibrationWithSystemCheck(35)

        PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_event)

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.delete_event -> {
                        val item: PagingModel.Data<EventUiModel>? =
                            getItem(position) as? PagingModel.Data

                        item?.value?.let(listener::onDeleteClicked)

                        context.singleVibrationWithSystemCheck(35)

                        true
                    }

                    R.id.update_event -> {
                        val item: PagingModel.Data<EventUiModel>? =
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

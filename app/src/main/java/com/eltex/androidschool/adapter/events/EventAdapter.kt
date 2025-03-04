package com.eltex.androidschool.adapter.events

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

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck

/**
 * Адаптер для отображения списка событий в RecyclerView.
 *
 * Этот класс отвечает за управление списком событий и их отображение в RecyclerView.
 * Он также обрабатывает события, такие как клики на кнопки "лайк", "поделиться" и "удалить".
 *
 * @param listener Слушатель событий, который будет вызываться при кликах на элементы списка.
 *
 * @see EventViewHolder ViewHolder, используемый для отображения элементов списка.
 * @see EventItemCallback Callback для сравнения элементов списка.
 */
class EventAdapter(
    private val listener: EventListener,
    private val context: Context,
    private val currentUserId: Long
) : ListAdapter<EventUiModel, EventViewHolder>(EventItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с Событиями.
     */
    interface EventListener {
        fun onLikeClicked(event: EventUiModel)
        fun onShareClicked(event: EventUiModel)
        fun onParticipateClicked(event: EventUiModel)
        fun onDeleteClicked(event: EventUiModel)
        fun onUpdateClicked(event: EventUiModel)
        fun onGetUserClicked(event: EventUiModel)
    }

    /**
     * Создает новый ViewHolder для отображения элемента списка.
     *
     * @param parent Родительский ViewGroup.
     * @param viewType Тип View.
     *
     * @return EventViewHolder Новый ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(layoutInflater, parent, false)

        val viewHolder = EventViewHolder(
            binding = binding,
            context = parent.context
        )

        binding.like.setOnClickListener {
            listener.onLikeClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.participate.setOnClickListener {
            listener.onParticipateClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.share.setOnClickListener {
            listener.onShareClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.avatar.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.author.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            showPopupMenu(view, viewHolder.bindingAdapterPosition)
        }

        return viewHolder
    }

    /**
     * Привязывает данные к существующему ViewHolder.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     */
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(
            event = getItem(position),
            currentUserId = currentUserId
        )
    }

    /**
     * Привязывает данные к существующему ViewHolder с учетом изменений.
     *
     * @param holder ViewHolder, к которому привязываются данные.
     * @param position Позиция элемента в списке.
     * @param payloads Список изменений.
     */
    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { event ->
                if (event is EventPayload) {
                    holder.bind(event)
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
    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showPopupMenu(view: View, position: Int) {
        context.singleVibrationWithSystemCheck(35)

        val resources = view.context.resources

        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        ).toInt()

        val popup = PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_event)

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
                    R.id.delete_event -> {
                        listener.onDeleteClicked(getItem(position))
                        context.singleVibrationWithSystemCheck(35)

                        true
                    }

                    R.id.update_event -> {
                        listener.onUpdateClicked(getItem(position))

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
}

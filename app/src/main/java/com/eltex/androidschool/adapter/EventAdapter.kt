package com.eltex.androidschool.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.R

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.databinding.CardEventBinding

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
@Suppress("DEPRECATION")
class EventAdapter(
    private val listener: EventListener,
    private val context: Context
) : ListAdapter<Event, EventViewHolder>(EventItemCallback()) {

    /**
     * Интерфейс для обработки событий, связанных с Событиями.
     */
    interface EventListener {
        fun onLikeClicked(event: Event)
        fun onShareClicked(event: Event)
        fun onParticipateClicked(event: Event)
        fun onDeleteClicked(event: Event)
        fun onUpdateClicked(event: Event)
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

        val viewHolder = EventViewHolder(binding, parent.context)

        binding.like.setOnClickListener {
            listener.onLikeClicked(getItem(viewHolder.adapterPosition))
        }

        binding.participate.setOnClickListener {
            listener.onParticipateClicked(getItem(viewHolder.adapterPosition))
        }

        binding.share.setOnClickListener {
            listener.onShareClicked(getItem(viewHolder.adapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            PopupMenu(view.context, view).apply {
                inflate(R.menu.menu_event)

                setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.delete_event -> {
                            listener.onDeleteClicked(getItem(viewHolder.adapterPosition))
                            vibrate()
                            true
                        }

                        R.id.update_event -> {
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
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(getItem(position))
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
     * Метод для вызова вибрации.
     */
    @SuppressLint("MissingPermission")
    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(50)
        }

//        val pattern = longArrayOf(0, 100, 200, 300) // Пауза 0 мс, вибрация 100 мс, пауза 200 мс, вибрация 300 мс
//        vibrator.vibrate(pattern, -1) // Повторение последовательности (-1 означает отсутствие повторений)
    }
}

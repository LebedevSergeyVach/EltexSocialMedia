package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.databinding.CardEventBinding

/**
 * Адаптер для отображения списка событий в RecyclerView.
 *
 * @param likeClickListener Функция, которая будет вызываться при клике на кнопку "лайк".
 * @param participateClickListener Функция, которая будет вызываться при клике на кнопку "участвовать".
 *
 * @see EventViewHolder ViewHolder, используемый для отображения элементов списка.
 * @see EventItemCallback Callback для сравнения элементов списка.
 */
class EventAdapter(
    private val likeClickListener: (event: Event) -> Unit,
    private val participateClickListener: (event: Event) -> Unit,
) : ListAdapter<Event, EventViewHolder>(EventItemCallback()) {

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

        // Обработчики кликов для кнопок like и participate
        binding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }

        binding.participate.setOnClickListener {
            participateClickListener(getItem(viewHolder.adapterPosition))
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
}

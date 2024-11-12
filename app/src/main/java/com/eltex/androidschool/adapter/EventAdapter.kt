package com.eltex.androidschool.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.databinding.CardEventBinding

class EventAdapter(
    private val likeClickListener: (event: Event) -> Unit,
    private val participateClickListener: (event: Event) -> Unit,
) : ListAdapter<Event, EventViewHolder>(EventItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardEventBinding.inflate(layoutInflater, parent, false)

        val viewHolder = EventViewHolder(binding, parent.context)

        binding.like.setOnClickListener {
            likeClickListener(getItem(viewHolder.adapterPosition))
        }

        binding.participate.setOnClickListener {
            participateClickListener(getItem(viewHolder.adapterPosition))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvent(getItem(position))
    }

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

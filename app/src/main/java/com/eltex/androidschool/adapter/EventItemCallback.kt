package com.eltex.androidschool.adapter

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.data.Event

class EventItemCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: Event, newItem: Event): Any? =
        EventPayload(
            likeByMe = newItem.likeByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likeByMe
            },
            participateByMe = newItem.participateByMe.takeIf { participateByMe: Boolean ->
                participateByMe != oldItem.participateByMe
            }
        )
            .takeIf { eventPayload: EventPayload ->
                eventPayload.isNotEmpty()
            }
}

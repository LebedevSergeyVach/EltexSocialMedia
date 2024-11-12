package com.eltex.androidschool.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R
import com.eltex.androidschool.data.Event
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.utils.toast
import com.github.jinatonic.confetti.CommonConfetti

class EventViewHolder(
    private val binding: CardEventBinding, private val context: Context
) : ViewHolder(binding.root) {
    fun bindEvent(event: Event) {
        binding.author.text = event.author
        binding.initial.text = event.author.take(1)
        binding.published.text = event.published
        binding.optionConducting.text = event.optionConducting
        binding.dataEvent.text = event.dataEvent
        binding.content.text = event.content
        binding.link.text = event.link

        updateLike(event.likeByMe)
        updateParticipate(event.participateByMe)

        binding.menu.setOnClickListener {
            context.toast(R.string.not_implemented)
        }

        binding.share.setOnClickListener {
            context.toast(R.string.not_implemented, false)
        }
    }

    fun bind(payload: EventPayload) {
        payload.likeByMe?.let { likeByMe: Boolean ->
            updateLike(likeByMe)

            buttonClickAnimation(binding.like, likeByMe)
        }
        payload.participateByMe?.let { participateByMe: Boolean ->
            updateParticipate(participateByMe)

            buttonClickAnimation(binding.participate, participateByMe)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateLike(likeByMe: Boolean) {
        binding.like.isSelected = likeByMe

        binding.like.text = if (likeByMe) {
            1
        } else {
            0
        }.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun updateParticipate(participateByMe: Boolean) {
        binding.participate.isSelected = participateByMe

        binding.participate.text = if (participateByMe) {
            1
        } else {
            0
        }.toString()
    }

    private fun buttonClickAnimation(button: View, condition: Boolean){
        if (condition) {
            val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_animation)

            button.startAnimation(animation)

            CommonConfetti.rainingConfetti(
                binding.root,
                intArrayOf(R.color.red)
            ).oneShot()
        }
    }
}

package com.eltex.androidschool.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R

import com.github.jinatonic.confetti.CommonConfetti

import com.eltex.androidschool.data.Event
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.databinding.CardEventBinding

/**
 * ViewHolder для отображения элемента списка событий.
 *
 * @param binding Binding для макета элемента списка.
 * @param context Контекст приложения.
 *
 * @see EventAdapter Адаптер, использующий этот ViewHolder.
 */
class EventViewHolder(
    private val binding: CardEventBinding, private val context: Context
) : ViewHolder(binding.root) {

    /**
     * Привязывает данные события к элементу списка.
     *
     * @param event Событие, данные которого нужно отобразить.
     */
    fun bindEvent(event: Event) {
        binding.author.text = event.author
        binding.initial.text = event.author.take(1)
        binding.published.text = event.getFormattedPublished()
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

    /**
     * Привязывает данные события к элементу списка с учетом изменений.
     *
     * @param payload Изменения в событии.
     */
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

    /**
     * Обновляет состояние лайка события.
     *
     * @param likeByMe Состояние лайка (лайкнут/не лайкнут).
     */
    @SuppressLint("SetTextI18n")
    private fun updateLike(likeByMe: Boolean) {
        binding.like.isSelected = likeByMe

        binding.like.text = if (likeByMe) {
            1
        } else {
            0
        }.toString()
    }

    /**
     * Обновляет состояние участия в событии.
     *
     * @param participateByMe Состояние участия (участвует/не участвует).
     */
    @SuppressLint("SetTextI18n")
    private fun updateParticipate(participateByMe: Boolean) {
        binding.participate.isSelected = participateByMe

        binding.participate.text = if (participateByMe) {
            1
        } else {
            0
        }.toString()
    }

    /**
     * Выполняет анимацию при клике на кнопку.
     *
     * @param button Кнопка, на которую был клик.
     * @param condition Условие для выполнения анимации.
     */
    private fun buttonClickAnimation(button: View, condition: Boolean){
        if (condition) {
            val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_animation)

            button.startAnimation(animation)

            CommonConfetti.rainingConfetti(
                binding.root,
                intArrayOf(R.color.red)
            ).oneShot()

            CommonConfetti.rainingConfetti(
                binding.root,
                intArrayOf(R.color.purple)
            ).oneShot()
        }
    }
}

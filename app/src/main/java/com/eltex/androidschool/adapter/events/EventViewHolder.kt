package com.eltex.androidschool.adapter.events

import android.animation.AnimatorInflater
import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent

import android.text.SpannableString

import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.github.jinatonic.confetti.CommonConfetti

import com.eltex.androidschool.R

import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.utils.toast

/**
 * ViewHolder для отображения элемента списка событий.
 *
 * @param binding Binding для макета элемента списка.
 * @param context Контекст приложения.
 *
 * @see EventAdapter Адаптер, использующий этот ViewHolder.
 */
@SuppressLint("ClickableViewAccessibility")
class EventViewHolder(
    private val binding: CardEventBinding, private val context: Context
) : ViewHolder(binding.root) {
    private var lastClickTime: Long = 0

    init {
        binding.cardEvent.setOnTouchListener { _, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < 300) {
                    onDoubleClick()
                }
                lastClickTime = clickTime
            }
            false
        }
    }

    private fun onDoubleClick() {
        binding.like.performClick()
    }

    /**
     * Привязывает данные события к элементу списка.
     *
     * @param event Событие, данные которого нужно отобразить.
     */
    fun bindEvent(event: EventData) {
        binding.author.text = event.author
        binding.initial.text = event.author.take(1)
        binding.published.text = event.getFormattedPublished()
        binding.optionConducting.text = event.optionConducting
        binding.dataEvent.text = event.getFormattedDataAndTimeEvent()
        binding.content.text = event.content
        binding.link.text = event.link

        SpannableString(binding.link.text)
        SpannableString(binding.content.text)

        if (event.lastModified != null) {
            binding.lastModified.visibility = View.VISIBLE
            binding.lastModified.text =
                event.getFormattedLastModified()?.let { lastModified: String? ->
                    context.getString(R.string.changed) + ": $lastModified"
                }
        } else {
            binding.lastModified.visibility = View.GONE
        }

        updateLike(event.likeByMe)
        updateParticipate(event.participateByMe)

        binding.share.setOnClickListener {
            context.toast(R.string.shared)

            var modification = ""

            if (event.lastModified != null) {
                modification =
                    "\n\n" + context.getString(R.string.changed) + ": " +
                            event.getFormattedLastModified()
            }

            val intent = Intent.createChooser(
                Intent(Intent.ACTION_SEND)
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        context.getString(R.string.author) + ":\n" + event.author
                                + "\n\n" + context.getString(R.string.published)
                                + ":\n" + event.getFormattedPublished()
                                + "\n\n" + context.getString(R.string.data_event) + ":\n"
                                + event.getFormattedDataAndTimeEvent()
                                + "\n\n" + event.optionConducting
                                + "\n\n" + context.getString(R.string.event) + ":\n" + event.content
                                + "\n\n" + context.getString(R.string.link) + ":\n" + event.link
                                + modification
                    )
                    .setType("text/plain"),
                null
            )


            runCatching {
                context.startActivity(intent)
            }.onFailure {
                context.toast(R.string.app_not_found, false)
            }

            buttonClickAnimation(binding.share, condition = true, confetti = true)
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

            buttonClickAnimation(binding.like, likeByMe, true)
        }
        payload.participateByMe?.let { participateByMe: Boolean ->
            updateParticipate(participateByMe)

            buttonClickAnimation(binding.participate, participateByMe, true)
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
     * @param condition Условие для выполнения анимации увеличения.
     * @param confetti Условие для выполнения анимации конфетти.
     *
     * @sample EventViewHolder.buttonClickAnimation
     */
    @SuppressLint("ResourceType")
    private fun buttonClickAnimation(button: View, condition: Boolean, confetti: Boolean) {
        if (condition) {
            val animator =
                AnimatorInflater.loadAnimator(binding.root.context, R.anim.scale_animation)

            animator.setTarget(button)
            animator.start()

            if (confetti) {
                val confettiColors = intArrayOf(
                    ContextCompat.getColor(binding.root.context, R.color.white),
                    ContextCompat.getColor(binding.root.context, R.color.confetti_blue),
                    ContextCompat.getColor(binding.root.context, R.color.blue),
                )

                CommonConfetti.rainingConfetti(
                    binding.root,
                    confettiColors
                ).oneShot()
            }
        }
    }
}

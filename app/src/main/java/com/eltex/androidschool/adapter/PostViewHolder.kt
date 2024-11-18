package com.eltex.androidschool.adapter

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eltex.androidschool.R

import com.github.jinatonic.confetti.CommonConfetti

import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.data.Post
import com.eltex.androidschool.utils.toast

/**
 * ViewHolder для отображения элемента списка постов.
 *
 * @param binding Binding для макета элемента списка.
 * @param context Контекст приложения.
 *
 * @see PostAdapter Адаптер, использующий этот ViewHolder.
 */
@SuppressLint("ClickableViewAccessibility")
class PostViewHolder(
    private val binding: CardPostBinding, private val context: Context
) : ViewHolder(binding.root) {
    private var lastClickTime: Long = 0

    init {
        binding.cardPost.setOnTouchListener { _, post: MotionEvent ->
            if (post.action == MotionEvent.ACTION_DOWN) {
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
     * Привязывает данные поста к элементам пользовательского интерфейса.
     *
     * @param post Пост, данные которого нужно отобразить.
     */
    fun bindPost(post: Post) {
        binding.author.text = post.author
        binding.initial.text = post.author.take(1)
        binding.published.text = post.getFormattedPublished()
        binding.content.text = post.content

        if (post.lastModified != null) {
            binding.lastModified.visibility = View.VISIBLE
            binding.lastModified.text =
                post.getFormattedLastModified()?.let { lastModified: String? ->
                    context.getString(R.string.changed) + ": $lastModified"
                }
        } else {
            binding.lastModified.visibility = View.GONE
        }

        updateLike(post.likeByMe)

        binding.share.setOnClickListener {
            context.toast(R.string.shared)

            val intent = Intent.createChooser(
                Intent(Intent.ACTION_SEND)
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        post.author + "\n\n" + post.getFormattedPublished() + "\n\n" + post.content
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
     * Привязывает данные поста к элементам пользовательского интерфейса с учетом изменений.
     *
     * @param payload Изменения в посте.
     */
    fun bind(payload: PostPayload) {
        payload.likeByMe?.let { likeByMe: Boolean ->
            updateLike(likeByMe)

            buttonClickAnimation(binding.like, likeByMe, true)
        }
    }

    /**
     * Обновляет состояние лайка поста.
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
     * Выполняет анимацию при клике на кнопку.
     *
     * @param button Кнопка, на которую был клик.
     * @param condition Условие для выполнения анимации увеличения.
     * @param confetti Условие для выполнения анимации конфетти.
     *
     * @sample PostViewHolder.buttonClickAnimation
     */
    @SuppressLint("ResourceType")
    private fun buttonClickAnimation(button: View, condition: Boolean, confetti: Boolean) {
        if (condition) {
            val animator =
                AnimatorInflater.loadAnimator(binding.root.context, R.anim.scale_animation)

            animator.setTarget(button)
            animator.start()

            if (confetti) {
                CommonConfetti.rainingConfetti(
                    binding.root,
                    intArrayOf(R.color.red)
                ).oneShot()

                CommonConfetti.rainingConfetti(
                    binding.root,
                    intArrayOf(R.color.blue)
                ).oneShot()
            }
        }
    }
}

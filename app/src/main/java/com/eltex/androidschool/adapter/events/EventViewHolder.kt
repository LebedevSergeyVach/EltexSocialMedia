package com.eltex.androidschool.adapter.events

import android.animation.AnimatorInflater

import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent

import android.graphics.drawable.Drawable

import android.text.SpannableString
import android.view.MotionEvent
import android.view.View

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.R
import com.eltex.androidschool.data.common.Attachment
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.ui.events.EventUiModel
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast

import com.github.jinatonic.confetti.CommonConfetti

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
    private val binding: CardEventBinding,
    private val context: Context
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
    @SuppressLint("SetTextI18n")
    fun bindEvent(event: EventUiModel, currentUserId: Long) {
        binding.author.text = event.author
        binding.published.text = event.published
        binding.optionConducting.text = event.optionConducting
        binding.dataEvent.text = event.dateEvent
        binding.content.text = event.content

        if (event.link.isNotEmpty()) {
            binding.link.isVisible = true
            binding.link.text = event.link
        } else {
            binding.link.isVisible = false
        }

        binding.like.text = event.likes.toString()
        binding.participate.text = event.participates.toString()

        val radius = context.resources.getDimensionPixelSize(R.dimen.radius_for_rounding_images)

        renderingUserAvatar(event = event)

         binding.skeletonAttachment.showSkeleton()

        if (event.attachment != null) {
            renderingImageAttachment(event.attachment, radius)
        } else {
            binding.skeletonAttachment.showOriginal()
            binding.attachment.isVisible = false
        }

        SpannableString(binding.link.text)
        SpannableString(binding.content.text)

        updateLike(event.likedByMe)
        updateParticipate(event.participatedByMe)

        binding.menu.isVisible = event.authorId == currentUserId

        binding.share.setOnClickListener {
            shareEvent(event)
        }

        binding.cardEvent.setOnLongClickListener {
            shareEvent(event)

            true
        }
    }

    /**
     * Отображает аватар пользователя в элементе интерфейса, используя библиотеку Glide.
     * Если аватар недоступен или его загрузка завершилась ошибкой, отображается заглушка и инициалы пользователя.
     *
     * @param event Экземпляр [EventUiModel], содержащий данные о посте, включая аватар автора и его имя.
     *
     * @see EventUiModel Модель данных поста, содержащая информацию об авторе и его аватаре.
     * @see Glide Библиотека для загрузки и отображения изображений.
     * @see RequestListener Интерфейс для обработки событий загрузки изображений.
     *
     * @property event.authorAvatar URL аватара автора. Может быть null или пустым.
     * @property event.author Имя автора, используемое для отображения инициалов, если аватар недоступен.
     * @property event.avatar ImageView для отображения аватара.
     * @property event.initial TextView для отображения инициалов автора.
     */
    private fun renderingUserAvatar(event: EventUiModel) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = event.author.take(1)
        binding.initial.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.initial.isVisible = true

        if (!event.authorAvatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(event.authorAvatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.avatar.setImageResource(R.drawable.avatar_background)
                        binding.initial.text = event.author.take(1)
                        binding.initial.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.white
                            )
                        )
                        binding.initial.isVisible = true

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.initial.isVisible = false

                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .error(R.drawable.error_placeholder)
                .thumbnail(
                    Glide.with(binding.root)
                        .load(event.authorAvatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            binding.avatar.setImageResource(R.drawable.avatar_background)
            binding.initial.text = event.author.take(1)
            binding.initial.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
            )
            binding.initial.isVisible = true
        }
    }

    /**
     * Отображает вложение (изображение) в элементе интерфейса, используя библиотеку Glide.
     * Если загрузка изображения завершилась ошибкой, отображается заглушка.
     * Изображение отображается с закругленными углами и эффектом перехода (cross-fade).
     *
     * @param attachment Экземпляр [Attachment], содержащий данные о вложении, включая URL изображения.
     * @param radius Радиус закругления углов изображения (в пикселях).
     *
     * @see Attachment Модель данных вложения, содержащая URL изображения.
     * @see Glide Библиотека для загрузки и отображения изображений.
     * @see RequestListener Интерфейс для обработки событий загрузки изображений.
     * @see RoundedCorners Трансформация для закругления углов изображения.
     * @see DrawableTransitionOptions Опции для анимации перехода при загрузке изображения.
     *
     * @property attachment.url URL изображения, которое необходимо загрузить.
     * @property binding.attachment ImageView для отображения вложения.
     * @property binding.skeletonAttachment Элемент интерфейса, используемый для отображения скелетона (заглушки) во время загрузки.
     */
    private fun renderingImageAttachment(
        attachment: Attachment,
        radius: Int
    ) {
        Glide.with(binding.root)
            .load(attachment.url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.skeletonAttachment.showOriginal()
                    binding.attachment.setImageResource(R.drawable.ic_404_24)

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.skeletonAttachment.showOriginal()

                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .transform(RoundedCorners(radius))
            .error(R.drawable.ic_404_24)
            .thumbnail(
                Glide.with(binding.root)
                    .load(attachment.url)
                    .override(50, 50)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(binding.attachment)
    }

    /**
     * Отправляет событие через сторонние приложения, поддерживающие обмен текстом.
     *
     * Этот метод создает интент для отправки текста события (автор, дата публикации и содержимое) через любое приложение,
     * которое поддерживает действие `Intent.ACTION_SEND`. Если подходящее приложение не найдено, пользователю будет показано
     * соответствующее уведомление. После успешного запуска интента выполняется анимация кнопки "поделиться" с эффектом конфетти
     * и вибрацией.
     *
     * @param event Объект `EventUiModel`, содержащий данные поста, которые будут переданы в интент. Включает автора, дату публикации
     *             и содержимое поста.
     *
     * @see Intent.ACTION_SEND Стандартное действие для отправки данных через другие приложения.
     * @see Intent.createChooser Создает диалог выбора приложения для отправки данных.
     * @see runCatching Обрабатывает возможные исключения при запуске интента.
     * @see buttonClickAnimation Выполняет анимацию кнопки с эффектом конфетти и вибрацией.
     */
    private fun shareEvent(event: EventUiModel) {
        context.toast(R.string.shared)

        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(R.string.author) + ":\n" + event.author
                            + "\n\n" + context.getString(R.string.published) + ":\n"
                            + event.published
                            + "\n\n" + context.getString(R.string.data_event) + ":\n"
                            + event.dateEvent
                            + "\n\n" + event.optionConducting
                            + "\n\n" + context.getString(R.string.event) + ":\n" + event.content
                            + "\n\n" + context.getString(R.string.link) + ":\n" + event.link
                )
                .setType("text/plain"),
            null
        )

        runCatching {
            context.startActivity(intent)
        }.onFailure {
            context.toast(R.string.app_not_found, false)
        }

        buttonClickAnimation(
            button = binding.share,
            condition = true,
            confetti = true,
            causeVibration = true
        )
    }

    /**
     * Привязывает данные события к элементу списка с учетом изменений.
     *
     * @param payload Изменения в событии.
     */
    @SuppressLint("SetTextI18n")
    fun bind(payload: EventPayload) {
        payload.likeByMe?.let { likeByMe: Boolean ->
            updateLike(likeByMe)

            buttonClickAnimation(
                button = binding.like,
                condition = likeByMe,
                confetti = likeByMe,
                causeVibration = true
            )
        }

        payload.likes?.let { likes: Int ->
            binding.like.text = likes.toString()
        }

        payload.participateByMe?.let { participateByMe: Boolean ->
            updateParticipate(participateByMe)

            buttonClickAnimation(
                button = binding.participate,
                condition = participateByMe,
                confetti = participateByMe,
                causeVibration = true
            )
        }

        payload.participates?.let { participates: Int ->
            binding.participate.text = participates.toString()
        }
    }

    /**
     * Обновляет состояние лайка события.
     *
     * @param likeByMe Состояние лайка (лайкнут/не лайкнут).
     */
    private fun updateLike(likeByMe: Boolean) {
        binding.like.isSelected = likeByMe
    }

    /**
     * Обновляет состояние участия в событии.
     *
     * @param participateByMe Состояние участия (участвует/не участвует).
     */
    private fun updateParticipate(participateByMe: Boolean) {
        binding.participate.isSelected = participateByMe
    }

    /**
     * Выполняет анимацию при клике на кнопку.
     *
     * @param button Кнопка, на которую был клик.
     * @param condition Условие для выполнения анимации увеличения (По умолчанию = false).
     * @param confetti Условие для выполнения анимации конфетти (По умолчанию = false).
     * @param causeVibration Вызов вибрации (По умолчанию = false).
     *
     * @sample EventViewHolder.buttonClickAnimation
     */
    @SuppressLint("ResourceType")
    private fun buttonClickAnimation(
        button: View,
        condition: Boolean = false,
        confetti: Boolean = false,
        causeVibration: Boolean = false
    ) {
        if (causeVibration) context.singleVibrationWithSystemCheck(35)

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

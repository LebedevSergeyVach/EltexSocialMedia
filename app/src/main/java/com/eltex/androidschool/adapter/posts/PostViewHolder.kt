package com.eltex.androidschool.adapter.posts

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
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.singleVibrationWithSystemCheck
import com.eltex.androidschool.utils.toast

import com.github.jinatonic.confetti.CommonConfetti

/**
 * ViewHolder для отображения элемента списка постов.
 * Этот класс отвечает за привязку данных поста к элементам пользовательского интерфейса.
 *
 * @param binding Binding для макета элемента списка.
 * @param context Контекст приложения.
 *
 * @property binding Привязка к макету `CardPostBinding`, который содержит UI для отображения поста.
 * @property context Контекст приложения.
 *
 * @see PostAdapter Адаптер, использующий этот ViewHolder.
 */
@SuppressLint("ClickableViewAccessibility")
class PostViewHolder(
    private val binding: CardPostBinding,
    private val context: Context
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
     * @param currentUserId ID текущего пользователя для определения прав на редактирование поста.
     */
    @SuppressLint("SetTextI18n")
    fun bindPost(post: PostUiModel, currentUserId: Long) {
        binding.author.text = post.author
        binding.content.text = post.content
        binding.published.text = post.published
        binding.like.text = post.likes.toString()

        val radius = context.resources.getDimensionPixelSize(R.dimen.radius_for_rounding_images)

        renderingUserAvatar(post = post)

        binding.skeletonAttachment.showSkeleton()

        if (post.attachment != null) {
            renderingImageAttachment(attachment = post.attachment, radius = radius)
        } else {
            binding.skeletonAttachment.showOriginal()
            binding.attachment.isVisible = false
        }

        SpannableString(binding.content.text)

        updateLike(likeByMe = post.likedByMe)

        binding.menu.isVisible = post.authorId == currentUserId

        binding.share.setOnClickListener {
            sharePost(post = post)
        }

        binding.cardPost.setOnLongClickListener {
            sharePost(post = post)
            true
        }
    }

    /**
     * Отображает аватар пользователя в элементе интерфейса, используя библиотеку Glide.
     * Если аватар недоступен или его загрузка завершилась ошибкой, отображается заглушка и инициалы пользователя.
     *
     * @param post Экземпляр [PostUiModel], содержащий данные о посте, включая аватар автора и его имя.
     *
     * @see PostUiModel Модель данных поста, содержащая информацию об авторе и его аватаре.
     * @see Glide Библиотека для загрузки и отображения изображений.
     * @see RequestListener Интерфейс для обработки событий загрузки изображений.
     *
     * @property post.authorAvatar URL аватара автора. Может быть null или пустым.
     * @property post.author Имя автора, используемое для отображения инициалов, если аватар недоступен.
     * @property binding.avatar ImageView для отображения аватара.
     * @property binding.initial TextView для отображения инициалов автора.
     */
    private fun renderingUserAvatar(post: PostUiModel) {
        if (!post.authorAvatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(post.authorAvatar)
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
                        binding.initial.text = post.author.take(1)
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
                .error(R.drawable.error_placeholder)
                .into(binding.avatar)
        } else {
            binding.avatar.setImageResource(R.drawable.avatar_background)
            binding.initial.text = post.author.take(1)
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
                    binding.attachment.setImageResource(R.drawable.error_placeholder)

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
            .transform(RoundedCorners(radius))
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.error_placeholder)
            .into(binding.attachment)
    }

    /**
     * Отправляет пост через сторонние приложения, поддерживающие обмен текстом.
     *
     * Этот метод создает интент для отправки текста поста (автор, дата публикации и содержимое) через любое приложение,
     * которое поддерживает действие `Intent.ACTION_SEND`.
     *
     * @param post Объект `PostUiModel`, содержащий данные поста, которые будут переданы в интент. Включает автора, дату публикации
     *             и содержимое поста.
     */
    private fun sharePost(post: PostUiModel) {
        context.toast(R.string.shared)

        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(R.string.author) + ":\n" + post.author
                            + "\n\n" + context.getString(R.string.published) + ":\n"
                            + post.published
                            + "\n\n" + context.getString(R.string.post) + ":\n" + post.content
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
     * Привязывает данные поста к элементам пользовательского интерфейса с учетом изменений.
     *
     * @param payload Изменения в посте.
     */
    @SuppressLint("SetTextI18n")
    fun bind(payload: PostPayload) {
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
    }

    /**
     * Обновляет состояние лайка поста.
     *
     * @param likeByMe Состояние лайка (лайкнут/не лайкнут).
     */
    private fun updateLike(likeByMe: Boolean) {
        binding.like.isSelected = likeByMe
    }

    /**
     * Выполняет анимацию при клике на кнопку.
     *
     * @param button Кнопка, на которую был клик.
     * @param condition Условие для выполнения анимации увеличения (По умолчанию = false).
     * @param confetti Условие для выполнения анимации конфетти (По умолчанию = false).
     * @param causeVibration Вызов вибрации (По умолчанию = false).
     *
     * @sample PostViewHolder.buttonClickAnimation
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

package com.eltex.androidschool.adapter.comments

import android.annotation.SuppressLint

import android.graphics.drawable.Drawable
import android.view.MotionEvent

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardCommentBinding
import com.eltex.androidschool.ui.comments.CommentUiModel
import com.eltex.androidschool.utils.common.initialsOfUsername
import com.eltex.androidschool.utils.extensions.buttonClickAnimationScale

@SuppressLint("ClickableViewAccessibility")
class CommentViewHolder(
    private val binding: CardCommentBinding,
) : ViewHolder(binding.root) {
    private var lastClickTime: Long = 0

    init {
        binding.cardComment.setOnTouchListener { _, comment: MotionEvent ->
            if (comment.action == MotionEvent.ACTION_DOWN) {
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

    fun bindComment(comment: CommentUiModel, currentUserId: Long) {
        binding.author.text = comment.author
        binding.published.text = comment.published
        binding.content.text = comment.content
        binding.like.text = comment.likes.toString()

        binding.menu.isVisible = comment.authorId == currentUserId

        updateLike(likeByMe = comment.likedByMe)
        renderingUserAvatar(comment = comment)
    }

    fun bindPayload(payload: CommentPayload) {
        payload.likeByMe?.let { likeByMe: Boolean ->
            updateLike(likeByMe)

            binding.root.context.buttonClickAnimationScale(
                button = binding.like,
                condition = likeByMe,
                causeVibration = true
            )
        }

        payload.likes?.let { likes: Int ->
            binding.like.text = likes.toString()
        }
    }

    private fun updateLike(likeByMe: Boolean) {
        binding.like.isSelected = likeByMe
    }

    fun renderingUserAvatar(comment: CommentUiModel) {
        showPlaceholder(comment = comment)
        binding.skeletonLayout.showSkeleton()

        if (!comment.authorAvatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(comment.authorAvatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showPlaceholder(comment = comment)

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.skeletonLayout.showOriginal()
                        binding.initial.isVisible = false

                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .thumbnail(
                    Glide.with(binding.root)
                        .load(comment.authorAvatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            showPlaceholder(comment = comment)
        }
    }

    private fun showPlaceholder(comment: CommentUiModel) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = initialsOfUsername(name = comment.author)
        binding.initial.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        binding.skeletonLayout.showOriginal()
        binding.initial.isVisible = true
    }
}

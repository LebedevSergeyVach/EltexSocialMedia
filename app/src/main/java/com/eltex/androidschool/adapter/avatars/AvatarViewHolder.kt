package com.eltex.androidschool.adapter.avatars

import android.graphics.drawable.Drawable

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
import com.eltex.androidschool.data.avatars.AvatarModel
import com.eltex.androidschool.databinding.ItemAvatarBinding
import com.eltex.androidschool.utils.common.initialsOfUsername

class AvatarViewHolder(
    private val binding: ItemAvatarBinding
) : ViewHolder(binding.root) {
    fun bindAvatar(avatarModel: AvatarModel) {
        renderingUserAvatar(avatarModel = avatarModel)
    }

    fun renderingUserAvatar(avatarModel: AvatarModel) {
        showPlaceholder(avatarModel = avatarModel)
        binding.skeletonLayout.showSkeleton()

        if (!avatarModel.avatar.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(avatarModel.avatar)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        showPlaceholder(avatarModel = avatarModel)

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
                        .load(avatarModel.avatar)
                        .override(50, 50)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
        } else {
            showPlaceholder(avatarModel = avatarModel)
        }
    }

    private fun showPlaceholder(avatarModel: AvatarModel) {
        binding.avatar.setImageResource(R.drawable.avatar_background)
        binding.initial.text = initialsOfUsername(name = avatarModel.name)
        binding.initial.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        binding.skeletonLayout.showOriginal()
        binding.initial.isVisible = true
    }
}

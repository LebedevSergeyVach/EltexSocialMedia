package com.eltex.androidschool.adapter.avatars

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.data.avatars.AvatarModel

class AvatarItemCallback : DiffUtil.ItemCallback<AvatarModel>() {
    override fun areItemsTheSame(oldItem: AvatarModel, newItem: AvatarModel): Boolean =
        oldItem.userId == newItem.userId

    override fun areContentsTheSame(oldItem: AvatarModel, newItem: AvatarModel): Boolean =
        oldItem == newItem
}

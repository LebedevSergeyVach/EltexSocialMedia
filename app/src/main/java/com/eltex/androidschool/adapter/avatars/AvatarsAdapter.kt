package com.eltex.androidschool.adapter.avatars

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.data.avatars.AvatarModel
import com.eltex.androidschool.databinding.ItemAvatarBinding

class AvatarsAdapter : ListAdapter<AvatarModel, AvatarViewHolder>(AvatarItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = AvatarViewHolder(binding = binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bindAvatar(avatarModel = getItem(position))
    }
}

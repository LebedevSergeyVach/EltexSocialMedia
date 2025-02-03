package com.eltex.androidschool.adapter.update

import androidx.recyclerview.widget.DiffUtil
import com.eltex.androidschool.data.update.UpdateData

class UpdateItemCallback : DiffUtil.ItemCallback<UpdateData>() {
    override fun areItemsTheSame(oldItem: UpdateData, newItem: UpdateData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UpdateData, newItem: UpdateData): Boolean =
        oldItem == newItem
}

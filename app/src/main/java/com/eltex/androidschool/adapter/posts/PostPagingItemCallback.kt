package com.eltex.androidschool.adapter.posts

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.ui.posts.PostPagingModel

class PostPagingItemCallback : DiffUtil.ItemCallback<PostPagingModel>() {
    private val delegate = PostItemCallback()

    override fun areItemsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: PostPagingModel, newItem: PostPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return false
        }

        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}

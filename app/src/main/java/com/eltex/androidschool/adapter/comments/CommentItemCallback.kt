package com.eltex.androidschool.adapter.comments

import androidx.recyclerview.widget.DiffUtil

import com.eltex.androidschool.ui.comments.CommentUiModel

class CommentItemCallback : DiffUtil.ItemCallback<CommentUiModel>() {

    override fun areItemsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: CommentUiModel, newItem: CommentUiModel): Any? =
        CommentPayload(
            likeByMe = newItem.likedByMe.takeIf { likeByMe: Boolean ->
                likeByMe != oldItem.likedByMe
            },
            likes = newItem.likes.takeIf { likes: Int ->
                likes != oldItem.likes
            },
        )
            .takeIf { commentPayload: CommentPayload ->
                commentPayload.isNotEmpty()
            }
}

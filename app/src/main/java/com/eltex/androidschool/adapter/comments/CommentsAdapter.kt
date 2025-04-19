package com.eltex.androidschool.adapter.comments

import android.annotation.SuppressLint

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu

import androidx.recyclerview.widget.ListAdapter

import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardCommentBinding
import com.eltex.androidschool.ui.comments.CommentUiModel
import com.eltex.androidschool.utils.extensions.singleVibrationWithSystemCheck

class CommentsAdapter(
    private val listener: CommentListener,
    private val context: Context,
    private val currentUserId: Long,
) : ListAdapter<CommentUiModel, CommentViewHolder>(CommentItemCallback()) {

    interface CommentListener {
        fun onLikeClicked(comment: CommentUiModel)
        fun onDeleteClicked(comment: CommentUiModel)
        fun onGetUserClicked(comment: CommentUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CardCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = CommentViewHolder(binding = binding)

        binding.like.setOnClickListener {
            listener.onLikeClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.avatar.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.author.setOnClickListener {
            listener.onGetUserClicked(getItem(viewHolder.bindingAdapterPosition))
        }

        binding.menu.setOnClickListener { view: View ->
            showPopupMenu(view = view, position = viewHolder.bindingAdapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bindComment(
            comment = getItem(position),
            currentUserId = currentUserId
        )
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, payloads: List<Any?>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { comment: Any? ->
                if (comment is CommentPayload) {
                    holder.bindPayload(payload = comment)
                }
            }
        } else {
            onBindViewHolder(holder = holder, position = position)
        }
    }

    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showPopupMenu(view: View, position: Int) {
        context.singleVibrationWithSystemCheck(35)

        val resources = view.context.resources

        val iconMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
        ).toInt()

        val popup = PopupMenu(view.context, view).apply {
            inflate(R.menu.menu_comment)

            if (menu is MenuBuilder) {
                val menuBuilder = menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)

                for (item in menuBuilder.visibleItems) {
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                        }
                    }
                }
            }

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.delete_comment -> {
                        listener.onDeleteClicked(getItem(position))
                        context.singleVibrationWithSystemCheck(35)

                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }

        popup.show()
    }
}

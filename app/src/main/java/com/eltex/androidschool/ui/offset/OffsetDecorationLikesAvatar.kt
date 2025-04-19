package com.eltex.androidschool.ui.offset

import android.graphics.Rect
import android.view.View

import androidx.annotation.Px

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OffsetDecorationLikesAvatar(
    @Px private val offsetTop: Int = 0,
    @Px private val offsetBottom: Int = 0,
    @Px private val offsetLeft: Int = 0,
    @Px private val offsetRight: Int = 0,
    private val orientation: Int,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (orientation == RecyclerView.VERTICAL) {
            outRect.top += offsetTop
            outRect.left += offsetLeft
            outRect.right += offsetRight

            val lastVisibleItemPosition =
                (parent.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()

            val lastItemPosition = parent.adapter?.itemCount?.minus(1)

            if (lastVisibleItemPosition == lastItemPosition) {
                outRect.bottom += offsetBottom
            }
        } else {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left += offsetLeft
            }
        }
    }
}

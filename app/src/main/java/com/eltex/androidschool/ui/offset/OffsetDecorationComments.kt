package com.eltex.androidschool.ui.offset

import android.graphics.Rect
import android.view.View

import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class OffsetDecorationComments(
    @Px private val offset: Int,
    @Px private val offsetVertical: Int = (offset + 2) / 2,
    @Px private val lastItemExtraBottomOffset: Int = 260
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top = offsetVertical
        outRect.left = offset
        outRect.right = offset

        val position = parent.getChildAdapterPosition(view)
        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = offsetVertical + lastItemExtraBottomOffset
        } else {
            outRect.bottom = offsetVertical
        }
    }
}

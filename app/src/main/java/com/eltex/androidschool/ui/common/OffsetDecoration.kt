package com.eltex.androidschool.ui.common

import android.graphics.Rect
import android.view.View

import androidx.annotation.Px

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Декорация для добавления отступов между элементами RecyclerView.
 *
 * @param offset Отступ между элементами.
 *
 * @see RecyclerView.ItemDecoration Базовый класс для декораций элементов RecyclerView.
 */
class OffsetDecoration(
    @Px private val offset: Int,
    @Px private val offsetVertical: Int = (offset + 2) / 2,
    private val horizontal: Boolean = true,
) : ItemDecoration() {

    /**
     * Устанавливает отступы для элемента списка.
     *
     * @param outRect Объект Rect, в который записываются отступы.
     * @param view View элемента списка.
     * @param parent RecyclerView, содержащий элементы.
     * @param state Состояние RecyclerView.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.top += offsetVertical
        outRect.bottom += offsetVertical

        if (horizontal) {
            outRect.left += offset
            outRect.right += offset
        }

//        val lastVisibleItemPosition =
//            (parent.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
//
//        val lastItemPosition = parent.adapter?.itemCount?.minus(1)
//
//        if (lastVisibleItemPosition == lastItemPosition) {
//            outRect.bottom += offset * 3
//        }
    }
}

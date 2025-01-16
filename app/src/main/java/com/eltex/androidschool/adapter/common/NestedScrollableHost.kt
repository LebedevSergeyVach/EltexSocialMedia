package com.eltex.androidschool.adapter.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.absoluteValue
import kotlin.math.sign

class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            ViewPager2.ORIENTATION_HORIZONTAL -> child?.canScrollHorizontally(direction) ?: false
            ViewPager2.ORIENTATION_VERTICAL -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException("Invalid orientation: $orientation")
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        handleInterceptTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    private fun handleInterceptTouchEvent(ev: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return

        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = ev.x
                initialY = ev.y
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - initialX
                val dy = ev.y - initialY
                val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL

                val scaledDx = dx.absoluteValue * if (isVpHorizontal) 1f else 1.5f
                val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1.5f else 1f

                if (scaledDx > touchSlop || scaledDy > touchSlop) {
                    if (isVpHorizontal == (scaledDx > scaledDy)) {
                        parent.requestDisallowInterceptTouchEvent(
                            !canChildScroll(
                                orientation,
                                if (isVpHorizontal) dx else dy
                            )
                        )
                    } else {
                        parent.requestDisallowInterceptTouchEvent(
                            canChildScroll(
                                orientation,
                                if (isVpHorizontal) dx else dy
                            )
                        )
                    }
                }
            }
        }
    }
}

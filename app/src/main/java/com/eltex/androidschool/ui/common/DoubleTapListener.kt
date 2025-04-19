package com.eltex.androidschool.ui.common

import android.view.GestureDetector
import android.view.MotionEvent

class DoubleTapListener(
    private val onSingleTap: () -> Unit,
    private val onDoubleTap: () -> Unit
) : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        onSingleTap()
        return true
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        onDoubleTap()
        return true
    }
}

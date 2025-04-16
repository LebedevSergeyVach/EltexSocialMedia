package com.eltex.androidschool.utils.extensions

import android.animation.AnimatorInflater
import android.annotation.SuppressLint

import android.content.Context
import android.view.View

import com.eltex.androidschool.R

@SuppressLint("ResourceType")
fun Context.buttonClickAnimationScale(
    button: View,
    condition: Boolean = false,
    causeVibration: Boolean = false,
) {
    if (causeVibration) this.singleVibrationWithSystemCheck(35)

    if (condition) {
        val animator =
            AnimatorInflater.loadAnimator(this, R.anim.scale_animation)

        animator.setTarget(button)
        animator.start()
    }
}

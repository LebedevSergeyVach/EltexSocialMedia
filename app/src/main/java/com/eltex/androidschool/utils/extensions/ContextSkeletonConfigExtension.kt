package com.eltex.androidschool.utils.extensions

import android.content.Context

import androidx.core.content.ContextCompat

import com.eltex.androidschool.R

import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.mask.SkeletonShimmerDirection

fun Context.createSkeletonConfig(
    maskColorRes: Int = R.color.gray,
    shimmerColorRes: Int = R.color.active_element,
    shimmerDurationInMillis: Long = 750,
    maskCornerRadius: Float = 10f,
    shimmerAngle: Int = 20,
    maskLayoutRes: Int? = null,
    showShimmer: Boolean = true,
    shimmerDirection: SkeletonShimmerDirection = SkeletonShimmerDirection.LEFT_TO_RIGHT
): SkeletonConfig {
    return SkeletonConfig(
        maskColor = ContextCompat.getColor(this, maskColorRes),
        maskCornerRadius = maskCornerRadius,
        shimmerColor = ContextCompat.getColor(this, shimmerColorRes),
        shimmerDurationInMillis = shimmerDurationInMillis,
        shimmerAngle = shimmerAngle,
        maskLayout = maskLayoutRes,
        showShimmer = showShimmer,
        shimmerDirection = shimmerDirection
    )
}

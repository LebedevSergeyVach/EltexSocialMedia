package com.eltex.androidschool.utils.extensions

import android.content.Context

import com.eltex.androidschool.R

fun Int.getCommentsText(context: Context): String =
    context.resources.getQuantityString(R.plurals.comments_count, this, this)

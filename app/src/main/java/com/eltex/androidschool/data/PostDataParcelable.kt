package com.eltex.androidschool.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс [PostDataParcelable], реализующий интерфейс [Parcelable] с использованием аннотации [Parcelize].
 * Используется для передачи данных о событии через [Intent].
 *
 * @property content Содержание события.
 * @property postId Уникальный идентификатор поста.
 *
 * @sample PostDataParcelable
 */
@Parcelize
data class PostDataParcelable(
    val content: String,
    val postId: Long,
) : Parcelable

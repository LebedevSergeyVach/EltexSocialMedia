package com.eltex.androidschool.data

import android.os.Parcel
import android.os.Parcelable

data class PostDataParcelable(
    val content: String,
    val postId: Long,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        content = parcel.readString() ?: "",
        postId = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeLong(postId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PostDataParcelable> {
        override fun createFromParcel(parcel: Parcel): PostDataParcelable {
            return PostDataParcelable(parcel)
        }

        override fun newArray(size: Int): Array<PostDataParcelable?> {
            return arrayOfNulls(size)
        }
    }

}

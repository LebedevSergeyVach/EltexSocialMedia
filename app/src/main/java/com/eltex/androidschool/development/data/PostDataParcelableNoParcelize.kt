package com.eltex.androidschool.development.data

import android.os.Parcel
import android.os.Parcelable

data class PostDataParcelableNoParcelize(
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

    companion object CREATOR : Parcelable.Creator<PostDataParcelableNoParcelize> {
        override fun createFromParcel(parcel: Parcel): PostDataParcelableNoParcelize {
            return PostDataParcelableNoParcelize(parcel)
        }

        override fun newArray(size: Int): Array<PostDataParcelableNoParcelize?> {
            return arrayOfNulls(size)
        }
    }

}

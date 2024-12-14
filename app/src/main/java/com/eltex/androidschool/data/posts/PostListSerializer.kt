package com.eltex.androidschool.data.posts

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.eltex.androidschool.data.PostList
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object PostListSerializer : Serializer<PostList> {
    override val defaultValue: PostList = PostList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PostList {
        try {
            return PostList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PostList, output: OutputStream) {
        t.writeTo(output)
    }
}

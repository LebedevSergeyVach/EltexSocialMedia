package com.eltex.androidschool.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object EventListSerializer : Serializer<EventList> {
    override val defaultValue: EventList = EventList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): EventList {
        try {
            return EventList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: EventList, output: OutputStream) {
        t.writeTo(output)
    }
}

package com.eltex.androidschool.data.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor

import java.time.Instant

/**
 * Сериализатор для класса [Instant].
 *
 * Этот сериализатор используется для преобразования объектов [Instant] в строку и обратно.
 * Он реализует интерфейс [KSerializer] из библиотеки Kotlinx Serialization.
 *
 * @see KSerializer Интерфейс для создания пользовательских сериализаторов.
 * @see Instant Класс, представляющий момент времени.
 */
class InstantSerializer : KSerializer<Instant> {

    /**
     * Описание сериализации для [Instant].
     *
     * Это описание указывает, что [Instant] будет сериализован как примитивный тип [PrimitiveKind.STRING].
     */
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    /**
     * Метод для десериализации строки в объект [Instant].
     *
     * @param decoder Декодер, используемый для чтения данных.
     * @return Объект [Instant], полученный из строки.
     */
    override fun deserialize(decoder: Decoder): Instant =
        Instant.parse(decoder.decodeString())

    /**
     * Метод для сериализации объекта [Instant] в строку.
     *
     * @param encoder Энкодер, используемый для записи данных.
     * @param value Объект [Instant], который нужно сериализовать.
     */
    override fun serialize(encoder: Encoder, value: Instant) =
        encoder.encodeString(value.toString())
}

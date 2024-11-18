package com.eltex.androidschool.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Класс [EventData], реализующий интерфейс [Parcelable].
 * Используется для передачи данных о событии через [Intent].
 *
 * @property content Содержание события.
 * @property date Дата события.
 * @property option Вариант проведения события.
 * @property link Ссылка на событие.
 * @property eventId Уникальный идентификатор события.
 *
 * @sample EventData
 */
data class EventData(
    val content: String,
    val date: String,
    val option: String,
    val link: String,
    val eventId: Long
) : Parcelable {
    /**
     * Вторичный конструктор, используемый для создания объекта из [Parcel].
     *
     * @param parcel [Parcel], из которого будут прочитаны данные.
     *
     * @sample EventData
     */
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()
    )

    /**
     * Метод для записи данных объекта в Parcel.
     *
     * @param parcel [Parcel], в который будут записаны данные.
     * @param flags Дополнительные флаги, указывающие, как должны быть записаны данные.
     *
     * @sample EventData.writeToParcel
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeString(option)
        parcel.writeString(link)
        parcel.writeLong(eventId)
    }

    /**
     * Метод, возвращающий специальные флаги для объекта.
     * В данном случае всегда возвращает 0, так как нет специальных флагов.
     *
     * @return 0, так как нет специальных флагов.
     *
     * @sample EventData.describeContents
     */
    override fun describeContents(): Int = 0

    /**
     * Статический объект [CREATOR], реализующий интерфейс [Parcelable.Creator].
     * Используется для создания объектов [EventData] из [Parcel].
     *
     * @sample EventData.CREATOR
     */
    companion object CREATOR : Parcelable.Creator<EventData> {

        /**
         * Метод для создания объекта [EventData] из [Parcel].
         *
         * @param parcel Parcel, из которого будут прочитаны данные.
         * @return Созданный объект EventData.
         *
         * @sample [EventData.createFromParcel]
         */
        override fun createFromParcel(parcel: Parcel): EventData {
            return EventData(parcel)
        }

        /**
         * Метод для создания массива объектов [EventData].
         *
         * @param size Размер массива.
         * @return Массив объектов [EventData].
         *
         * @sample EventData.newArray
         */
        override fun newArray(size: Int): Array<EventData?> {
            return arrayOfNulls(size)
        }
    }
}

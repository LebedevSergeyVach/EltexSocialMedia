package com.eltex.androidschool.development.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Класс [EventDataParcelableNoParcelize], реализующий интерфейс [Parcelable].
 * Используется для передачи данных о событии через [Intent].
 *
 * @property content Содержание события.
 * @property date Дата события.
 * @property option Вариант проведения события.
 * @property link Ссылка на событие.
 * @property eventId Уникальный идентификатор события.
 *
 * @sample EventDataParcelableNoParcelize
 */
data class EventDataParcelableNoParcelize(
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
     * @sample EventDataParcelableNoParcelize
     */
    constructor(parcel: Parcel) : this(
        content = parcel.readString() ?: "",
        date = parcel.readString() ?: "",
        option = parcel.readString() ?: "",
        link = parcel.readString() ?: "",
        eventId = parcel.readLong()
    )

    /**
     * Метод для записи данных объекта в Parcel.
     *
     * @param parcel [Parcel], в который будут записаны данные.
     * @param flags Дополнительные флаги, указывающие, как должны быть записаны данные.
     *
     * @sample EventDataParcelableNoParcelize.writeToParcel
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
     * @sample EventDataParcelableNoParcelize.describeContents
     */
    override fun describeContents(): Int = 0

    /**
     * Статический объект [CREATOR], реализующий интерфейс [Parcelable.Creator].
     * Используется для создания объектов [EventDataParcelableNoParcelize] из [Parcel].
     *
     * @sample EventDataParcelableNoParcelize.CREATOR
     */
    companion object CREATOR : Parcelable.Creator<EventDataParcelableNoParcelize> {

        /**
         * Метод для создания объекта [EventDataParcelableNoParcelize] из [Parcel].
         *
         * @param parcel Parcel, из которого будут прочитаны данные.
         * @return Созданный объект EventData.
         *
         * @sample [EventDataParcelableNoParcelize.createFromParcel]
         */
        override fun createFromParcel(parcel: Parcel): EventDataParcelableNoParcelize {
            return EventDataParcelableNoParcelize(parcel)
        }

        /**
         * Метод для создания массива объектов [EventDataParcelableNoParcelize].
         *
         * @param size Размер массива.
         * @return Массив объектов [EventDataParcelableNoParcelize].
         *
         * @sample EventDataParcelableNoParcelize.newArray
         */
        override fun newArray(size: Int): Array<EventDataParcelableNoParcelize?> {
            return arrayOfNulls(size)
        }
    }
}

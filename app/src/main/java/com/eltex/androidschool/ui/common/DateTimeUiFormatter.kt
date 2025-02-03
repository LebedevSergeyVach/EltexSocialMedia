package com.eltex.androidschool.ui.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Класс для форматирования даты и времени в строку.
 *
 * Этот класс используется для преобразования [Instant] в строку с учетом указанной временной зоны.
 * Форматирование выполняется в формате "dd.MM.yy HH:mm".
 *
 * @property zoneId Временная зона, используемая для форматирования даты и времени.
 *
 * @see Instant Момент времени, который форматируется.
 * @see ZoneId Временная зона, используемая для форматирования.
 */
class DateTimeUiFormatter @Inject constructor(private val zoneId: ZoneId) {

    private companion object {
        /**
         * Форматтер для преобразования даты и времени в строку.
         *
         * Используется для форматирования даты и времени в формате "dd.MM.yy HH:mm".
         *
         * @see DateTimeFormatter Класс, предоставляющий методы для форматирования даты и времени.
         */
        private const val FORMAT_DATE_TIME = "dd.MM.yy HH:mm"
    }

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME)

    /**
     * Форматирует [Instant] в строку с учетом временной зоны.
     *
     * Этот метод преобразует момент времени ([Instant]) в строку, используя указанную временную зону и формат "dd.MM.yy HH:mm".
     *
     * @param instant Момент времени, который нужно отформатировать.
     *
     * @return Отформатированная строка даты и времени.
     *
     * @throws DateTimeException Если форматирование не удалось из-за недопустимых данных.
     */
    fun format(instant: Instant?): String {
        return formatter.format(instant?.atZone(zoneId))
    }
}

package com.eltex.androidschool.di

import com.eltex.androidschool.ui.common.DateTimeUiFormatter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import java.time.ZoneId
import javax.inject.Singleton

/**
 * Модуль Dagger Hilt для предоставления зависимостей, связанных с форматированием даты и времени.
 *
 * Этот модуль предоставляет экземпляры классов, необходимых для работы с временными зонами и форматированием даты.
 * Все зависимости предоставляются в рамках `SingletonComponent`, что гарантирует их единственность в течение
 * всего жизненного цикла приложения.
 *
 * @see dagger.hilt.components.SingletonComponent Компонент, в рамках которого предоставляются зависимости.
 * @see dagger.Module Аннотация, указывающая, что класс является модулем Dagger.
 * @see dagger.Provides Аннотация, указывающая, что метод предоставляет зависимость.
 */
@Module
@InstallIn(SingletonComponent::class)
object DateTimeFormatterModule {

    /**
     * Предоставляет экземпляр [ZoneId], представляющий системную временную зону по умолчанию.
     *
     * Этот метод используется для получения временной зоны, которая будет использоваться в приложении
     * для форматирования даты и времени. По умолчанию используется системная временная зона.
     *
     * @return Экземпляр [ZoneId], представляющий системную временную зону.
     *
     * @see ZoneId.systemDefault() Метод, возвращающий системную временную зону.
     */
    @Provides
    @Singleton
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()

    /**
     * Предоставляет экземпляр [DateTimeUiFormatter], используемый для форматирования даты и времени.
     *
     * Этот метод создает и возвращает экземпляр [DateTimeUiFormatter], который используется для преобразования
     * [Instant] в строку с учетом указанной временной зоны.
     *
     * @param zoneId Временная зона, используемая для форматирования даты и времени.
     *
     * @return Экземпляр [DateTimeUiFormatter], настроенный на использование указанной временной зоны.
     *
     * @see DateTimeUiFormatter Класс, отвечающий за форматирование даты и времени.
     * @see ZoneId Временная зона, используемая для форматирования.
     */
    @Provides
    @Singleton
    fun provideDateFormatter(zoneId: ZoneId): DateTimeUiFormatter = DateTimeUiFormatter(zoneId)
}

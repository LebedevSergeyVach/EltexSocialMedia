package com.eltex.androidschool.viewmodel.events.newevent

import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние ViewModel для создания или обновления события.
 *
 * @property event Обновленное или созданное событие. По умолчанию null.
 * @property statusEvent Состояние операции. По умолчанию Idle.
 * @property file Модель файла, который может быть прикреплен к посту. Может быть null, если вложение отсутствует.
 */
data class NewEventState(
    val event: EventData? = null,
    val statusEvent: StatusLoad = StatusLoad.Idle,
    val file: FileModel? = null,
)

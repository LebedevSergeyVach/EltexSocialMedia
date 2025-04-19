package com.eltex.androidschool.ui.events

import com.eltex.androidschool.data.avatars.AvatarModel
import com.eltex.androidschool.data.events.EventData
import com.eltex.androidschool.data.users.UserPreview
import com.eltex.androidschool.ui.common.DateTimeUiFormatter

import javax.inject.Inject

/**
 * Маппер для преобразования данных события ([EventData]) в модель UI ([EventUiModel]).
 *
 * Этот класс используется для преобразования данных, полученных из API, в формат,
 * подходящий для отображения в пользовательском интерфейсе.
 *
 * @property dateTimeUiFormatter Форматтер даты и времени, используемый для преобразования [Instant] в строку.
 *
 * @see EventData Класс, представляющий данные события.
 * @see EventUiModel Класс, представляющий модель UI для события.
 * @see DateTimeUiFormatter Класс, отвечающий за форматирование даты и времени.
 */
class EventUiModelMapper @Inject constructor(
    private val dateTimeUiFormatter: DateTimeUiFormatter
) {

    /**
     * Преобразует объект [EventData] в объект [EventUiModel].
     *
     * Этот метод преобразует данные события в формат, подходящий для отображения в UI.
     *
     * @param event Объект [EventData], который нужно преобразовать.
     * @return Объект [EventUiModel], представляющий событие в UI.
     */
    fun map(event: EventData, mapListAvatarModel: Boolean = false): EventUiModel = with(event) {
        if (mapListAvatarModel) {
            EventUiModel(
                id = id,
                author = author,
                authorId = authorId,
                authorAvatar = authorAvatar,
                authorJob = authorJob,
                published = dateTimeUiFormatter.format(instant = published),
                optionConducting = optionConducting,
                dateEvent = dateTimeUiFormatter.format(instant = dataEvent),
                content = content,
                link = link,
                likedByMe = likedByMe,
                likes = likeOwnerIds.size,
                participatedByMe = participatedByMe,
                participates = participantsIds.size,
                attachment = attachment,
                likesListUsers = users.filter { userMap: Map.Entry<Long, UserPreview> ->
                    likeOwnerIds.contains(userMap.key)
                }.map { userMap: Map.Entry<Long, UserPreview> ->
                    AvatarModel(userMap.key, userMap.value.name, userMap.value.avatar)
                },
                participationListUsers = users.filter { userMap: Map.Entry<Long, UserPreview> ->
                    participantsIds.contains(userMap.key)
                }.map { userMap: Map.Entry<Long, UserPreview> ->
                    AvatarModel(userMap.key, userMap.value.name, userMap.value.avatar)
                },
            )
        } else {
            EventUiModel(
                id = id,
                author = author,
                authorId = authorId,
                authorAvatar = authorAvatar,
                published = dateTimeUiFormatter.format(instant = published),
                optionConducting = optionConducting,
                dateEvent = dateTimeUiFormatter.format(instant = dataEvent),
                content = content,
                link = link,
                likedByMe = likedByMe,
                likes = likeOwnerIds.size,
                participatedByMe = participatedByMe,
                participates = participantsIds.size,
                attachment = attachment,
            )
        }
    }
}

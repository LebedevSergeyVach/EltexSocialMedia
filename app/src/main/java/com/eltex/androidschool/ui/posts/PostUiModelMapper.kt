package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.data.avatars.AvatarModel
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.data.users.UserPreview
import com.eltex.androidschool.ui.common.DateTimeUiFormatter

import javax.inject.Inject

/**
 * Маппер для преобразования данных поста ([PostData]) в модель UI ([PostUiModel]).
 *
 * Этот класс используется для преобразования данных, полученных из API, в формат,
 * подходящий для отображения в пользовательском интерфейсе.
 *
 * @property dateTimeUiFormatter Форматтер даты и времени, используемый для преобразования [Instant] в строку.
 *
 * @see PostData Класс, представляющий данные поста.
 * @see PostUiModel Класс, представляющий модель UI для поста.
 * @see DateTimeUiFormatter Класс, отвечающий за форматирование даты и времени.
 */
class PostUiModelMapper @Inject constructor(
    private val dateTimeUiFormatter: DateTimeUiFormatter
) {

    /**
     * Преобразует объект [PostData] в объект [PostUiModel].
     *
     * Этот метод преобразует данные поста в формат, подходящий для отображения в UI.
     *
     * @param post Объект [PostData], который нужно преобразовать.
     * @return Объект [PostUiModel], представляющий пост в UI.
     */
    fun map(post: PostData, mapListAvatarModel: Boolean = false): PostUiModel = with(post) {
        if (mapListAvatarModel) {
            PostUiModel(
                id = id,
                content = content,
                author = author,
                authorJob = authorJob,
                authorId = authorId,
                authorAvatar = authorAvatar,
                published = dateTimeUiFormatter.format(instant = published),
                likedByMe = likedByMe,
                likes = likeOwnerIds.size,
                attachment = attachment,
                likesListUsers = users.filter { userMap: Map.Entry<Long, UserPreview> ->
                    likeOwnerIds.contains(userMap.key)
                }.map { userMap: Map.Entry<Long, UserPreview> ->
                    AvatarModel(userMap.key, userMap.value.name, userMap.value.avatar)
                },
            )
        } else {
            PostUiModel(
                id = id,
                content = content,
                author = author,
                authorJob = authorJob,
                authorId = authorId,
                authorAvatar = authorAvatar,
                published = dateTimeUiFormatter.format(instant = published),
                likedByMe = likedByMe,
                likes = likeOwnerIds.size,
                attachment = attachment,
            )
        }
    }
}

package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.data.avatars.AvatarModel
import com.eltex.androidschool.data.common.Attachment

/**
 * Data-класс, представляющий UI-модель поста.
 *
 * Этот класс используется для отображения информации о посте в пользовательском интерфейсе.
 * Он содержит все необходимые данные для отображения поста, включая текст, автора, дату публикации, лайки и вложения.
 *
 * @property id Уникальный идентификатор поста. Используется для однозначной идентификации поста в системе.
 * @property author Имя автора поста. Отображается в UI как создатель поста.
 * @property authorId Уникальный идентификатор автора поста. Используется для связи с профилем автора.
 * @property authorAvatar URL аватара автора поста. Может быть null, если аватар не задан. Используется для отображения изображения автора.
 * @property published Дата и время публикации поста в формате строки. Отображается в UI как время создания поста.
 * @property content Текстовое содержимое поста. Основной текст, который видит пользователь.
 * @property likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост. Используется для отображения состояния лайка в UI.
 * @property likes Количество лайков, полученных постом. Отображается в UI как количество пользователей, оценивших пост.
 * @property attachment Вложение, связанное с постом. Может быть null, если пост не содержит вложений. Используется для отображения медиафайлов (например, изображений) в посте.
 *
 * @see Attachment Класс, представляющий вложение в посте.
 */
data class PostUiModel(
    val id: Long = 0L,
    val author: String = "",
    val authorId: Long = 0L,
    val authorAvatar: String? = null,
    val authorJob: String? = null,
    val published: String = "",
    val content: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val attachment: Attachment? = null,
    val likesListUsers: List<AvatarModel> = emptyList()
)

package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.data.posts.PostData
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
    fun map(post: PostData): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            authorId = authorId,
            authorAvatar = authorAvatar,
            published = dateTimeUiFormatter.format(instant = published),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            attachment = attachment,
        )
    }
}

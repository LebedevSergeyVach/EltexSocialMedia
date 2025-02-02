package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.data.posts.PostData

import java.time.ZoneId
import java.time.format.DateTimeFormatter

import javax.inject.Inject

/**
 * Маппер для преобразования данных поста (PostData) в модель UI (PostUiModel).
 *
 * Этот класс используется для преобразования данных, полученных из API, в формат,
 * подходящий для отображения в пользовательском интерфейсе.
 *
 * @see PostData Класс, представляющий данные поста.
 * @see PostUiModel Класс, представляющий модель UI для поста.
 */
class PostUiModelMapper @Inject constructor() {
    private companion object {
        /**
         * Форматтер для преобразования даты и времени в строку.
         *
         * Используется для форматирования даты и времени в формате "dd.MM.yy HH.mm".
         */
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    /**
     * Преобразует объект PostData в объект PostUiModel.
     *
     * Этот метод преобразует данные поста в формат, подходящий для отображения в UI.
     *
     * @param post Объект PostData, который нужно преобразовать.
     * @return Объект PostUiModel, представляющий пост в UI.
     */
    fun map(post: PostData): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            authorId = authorId,
            authorAvatar = authorAvatar,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
            attachment = attachment,
        )
    }
}

package com.eltex.androidschool.viewmodel.posts.newposts

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * Состояние ViewModel для создания или обновления поста.
 *
 * Этот класс представляет состояние экрана создания или редактирования поста. Он содержит данные о посте, статус операции и файл вложения.
 *
 * @property post Обновленный или созданный пост. Может быть null, если пост еще не создан или не обновлен.
 * @property statusPost Состояние операции создания или обновления поста. По умолчанию Idle (ожидание).
 * @property file Модель файла, который может быть прикреплен к посту. Может быть null, если вложение отсутствует.
 *
 * @see PostData Класс, представляющий данные поста.
 * @see StatusLoad Перечисление, представляющее состояние загрузки (Idle, Loading, Error).
 * @see FileModel Класс, представляющий модель файла для вложения.
 */
data class NewPostState(
    val post: PostData? = null,
    val statusPost: StatusLoad = StatusLoad.Idle,
    val file: FileModel? = null,
) {
    /**
     * Флаг, указывающий, выполняется ли загрузка.
     */
    val isLoading: Boolean
        get() = statusPost == StatusLoad.Loading

    /**
     * Флаг, указывающий, успешно ли выполнена регистрация.
     */
    val isSuccess: Boolean
        get() = statusPost == StatusLoad.Success
}

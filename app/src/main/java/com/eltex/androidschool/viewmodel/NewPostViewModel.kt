package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.repository.PostRepository

/**
 * ViewModel для управления созданием и обновлением постов.
 *
 * Этот ViewModel отвечает за сохранение нового поста или обновление существующего.
 *
 * @see ViewModel Базовый класс для ViewModel, использующих функции библиотеки поддержки.
 * @see PostRepository Репозиторий для работы с данными постов.
 */
class NewPostViewModel(
    private val repository: PostRepository,
    private val postId: Long = 0L,
) : ViewModel() {

    /**
     * Сохраняет или обновляет пост.
     *
     * Если идентификатор поста равен 0, создается новый пост.
     * В противном случае обновляется существующий пост.
     *
     * @param content Содержимое поста.
     */
    fun save(content: String) {
        if (postId == 0L) {
            repository.addPost(content = content)
        } else {
            repository.updateById(
                postId = postId,
                content = content
            )
        }
    }
}
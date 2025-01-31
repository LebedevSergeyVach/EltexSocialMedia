package com.eltex.androidschool.viewmodel.posts.newposts

import android.content.ContentResolver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.viewmodel.common.FileModel
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.viewmodel.status.StatusLoad

/**
 * ViewModel для управления созданием и обновлением постов.
 *
 * Этот ViewModel отвечает за логику создания нового поста или обновления существующего. Он взаимодействует с репозиторием для сохранения данных и управляет состоянием экрана.
 *
 * @param repository Репозиторий для работы с данными постов. Используется для сохранения и обновления постов.
 * @param postId Идентификатор поста. По умолчанию 0, если создается новый пост. Если указан, то происходит обновление существующего поста.
 *
 * @see ViewModel Базовый класс для ViewModel, предоставляющий жизненный цикл и scope для корутин.
 * @see PostRepository Интерфейс репозитория для работы с данными постов.
 * @see NewPostState Состояние, управляемое этим ViewModel.
 */
class NewPostViewModel(
    private val repository: PostRepository,
    private val postId: Long = 0L,
) : ViewModel() {

    /**
     * Flow, хранящий текущее состояние создания или обновления поста.
     *
     * Этот Flow предоставляет доступ к текущему состоянию экрана создания/редактирования поста. Состояние включает данные поста, статус операции и файл вложения.
     *
     * @see NewPostState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(NewPostState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию создания или обновления поста.
     *
     * Этот Flow используется для наблюдения за изменениями состояния в UI. Он предоставляет только чтение состояния.
     *
     * @see NewPostState Состояние, которое предоставляется этим Flow.
     */
    val state = _state.asStateFlow()

    /**
     * Сохраняет или обновляет пост.
     *
     * Этот метод отвечает за сохранение нового поста или обновление существующего. Если идентификатор поста равен 0, создается новый пост. В противном случае обновляется существующий пост.
     *
     * @param content Содержимое поста. Текст, который будет сохранен или обновлен.
     * @param context Контекст приложения. Используется для работы с файлами и другими ресурсами.
     *
     * @see PostData Класс, представляющий данные поста.
     * @see StatusLoad Перечисление, представляющее состояние загрузки (Idle, Loading, Error).
     */
    fun save(content: String, contentResolver: ContentResolver, onProgress: (Int) -> Unit) {
        _state.update { newPostState: NewPostState ->
            newPostState.copy(
                statusPost = StatusLoad.Loading
            )
        }

        viewModelScope.launch {
            try {
                val post: PostData = repository.
                save(
                    postId = postId,
                    content = content,
                    fileModel = _state.value.file,
                    contentResolver = contentResolver,
                    onProgress = onProgress,
                )

                _state.update { newPostState: NewPostState ->
                    newPostState.copy(
                        statusPost = StatusLoad.Idle,
                        post = post,
                    )
                }
            } catch (e: Exception) {
                _state.update { newPostState: NewPostState ->
                    newPostState.copy(
                        statusPost = StatusLoad.Error(exception = e)
                    )
                }
            }
        }
    }

    /**
     * Сохраняет тип файла вложения.
     *
     * Этот метод обновляет состояние ViewModel, сохраняя модель файла, который будет прикреплен к посту.
     *
     * @param file Модель файла, который будет прикреплен к посту. Может быть null, если вложение отсутствует.
     *
     * @see FileModel Класс, представляющий модель файла для вложения.
     */
    fun saveAttachmentFileType(file: FileModel?) {
        _state.update { stateNewPost: NewPostState ->
            stateNewPost.copy(
                file = file,
            )
        }
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     *
     * Этот метод вызывается для сброса состояния ошибки и возврата в состояние Idle. Используется после обработки ошибки в UI.
     */
    fun consumerError() {
        _state.update { newPostState: NewPostState ->
            newPostState.copy(
                statusPost = StatusLoad.Idle
            )
        }
    }

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с корутинами. Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see viewModelScope Scope для корутин, связанных с жизненным циклом ViewModel.
     */
    override fun onCleared() {
        viewModelScope.cancel()
    }
}

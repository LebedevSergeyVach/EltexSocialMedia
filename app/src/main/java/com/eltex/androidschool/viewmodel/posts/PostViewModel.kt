package com.eltex.androidschool.viewmodel.posts

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.rx.common.SchedulersProvider
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel для управления состоянием постов и взаимодействия с репозиторием.
 *
 * @param repository Репозиторий, который предоставляет данные о постах.
 *
 * @see PostRepository Интерфейс репозитория, который используется в этом ViewModel.
 * @see PostState Состояние, которое управляется этим ViewModel.
 */
class PostViewModel(
    private val repository: PostRepository,
    private val schedulersProvider: SchedulersProvider = SchedulersProvider.DEFAULT,
) : ViewModel() {

    /**
     * Маппер для преобразования данных поста в UI-модель.
     *
     * @see PostUiModelMapper Класс, отвечающий за преобразование данных в UI-модель.
     */
    private val mapper = PostUiModelMapper()

    /**
     * Композитный disposable для управления подписками RxJava.
     *
     * Используется для хранения всех подписок и их последующего освобождения при очистке ViewModel.
     */
    private val disposable: CompositeDisposable = CompositeDisposable()

    /**
     * Flow, хранящий текущее состояние постов.
     *
     * @see PostState Состояние, которое хранится в этом Flow.
     */
    private val _state = MutableStateFlow(PostState())

    /**
     * Публичный Flow, который предоставляет доступ к текущему состоянию постов.
     *
     * @see PostState Состояние, которое предоставляется этим Flow.
     */
    val state: StateFlow<PostState> = _state.asStateFlow()

    /**
     * Инициализатор ViewModel.
     * Подписывается на изменения в Flow репозитория и обновляет состояние постов.
     */
    init {
        load()
    }

    /**
     * Загружает список постов с сервера.
     */
    fun load() {
        _state.update { statePost: PostState ->
            statePost.copy(
                statusPost = StatusPost.Loading
            )
        }

        repository.getPosts()
            .observeOn(schedulersProvider.computation)
            .map { posts: List<PostData> ->
                posts.map { post: PostData ->
                    mapper.map(post)
                }
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { allPosts: List<PostUiModel> ->
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Idle,
                            posts = allPosts,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
    }

    /**
     * Помечает пост с указанным идентификатором как "лайкнутый" или "нелайкнутый".
     *
     * @param postId Идентификатор поста, который нужно лайкнуть.
     * @param likedByMe Флаг, указывающий, лайкнул ли текущий пользователь этот пост.
     */
    fun likeById(postId: Long, likedByMe: Boolean) {
        repository.likeById(
            postId = postId,
            likedByMe = likedByMe
        )
            .observeOn(schedulersProvider.computation)
            .map { post: PostData ->
                _state.value.posts.orEmpty().map { postUiModel: PostUiModel ->
                    if (postUiModel.id == post.id) {
                        mapper.map(post)
                    } else {
                        postUiModel
                    }
                }
            }
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onSuccess = { posts: List<PostUiModel> ->
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Idle,
                            posts = posts,
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
    }

    /**
     * Удаляет пост по его идентификатору.
     *
     * @param postId Идентификатор поста, который нужно удалить.
     */
    fun deleteById(postId: Long) {
        _state.update { statePost: PostState ->
            statePost.copy(
                statusPost = StatusPost.Loading
            )
        }

        repository.deleteById(postId = postId)
            .observeOn(schedulersProvider.mainThread)
            .subscribeBy(
                onComplete = {
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Idle,
                            posts = _state.value.posts.orEmpty().filter { post: PostUiModel ->
                                post.id != postId
                            }
                        )
                    }
                },

                onError = { throwable: Throwable ->
                    _state.update { statePost: PostState ->
                        statePost.copy(
                            statusPost = StatusPost.Error(throwable = throwable)
                        )
                    }
                }
            )
            .addTo(disposable)
    }

    /**
     * Обрабатывает ошибку и сбрасывает состояние загрузки.
     */
    fun consumerError() {
        _state.update { postState: PostState ->
            postState.copy(
                statusPost = StatusPost.Idle
            )
        }
    }

    /**
     * Вызывается при очистке ViewModel.
     *
     * Этот метод освобождает все ресурсы, связанные с подписками RxJava.
     * Он вызывается, когда ViewModel больше не используется и будет уничтожено.
     *
     * @see CompositeDisposable Используется для управления подписками RxJava.
     */
    override fun onCleared() {
        disposable.dispose()
    }
}

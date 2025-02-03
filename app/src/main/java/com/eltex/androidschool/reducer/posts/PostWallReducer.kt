package com.eltex.androidschool.reducer.posts

import arrow.core.Either

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.effects.posts.PostWallEffect
import com.eltex.androidschool.model.posts.PostWithError
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.Logger
import com.eltex.androidschool.viewmodel.posts.post.PostStatus
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState

import javax.inject.Inject

/**
 * Редьюсер для управления состоянием постов конкретного пользователя (стены пользователя).
 * Этот класс обрабатывает сообщения (Intents) и обновляет состояние постов на основе этих сообщений.
 *
 * @property userId Идентификатор пользователя, чьи посты загружаются и обрабатываются.
 *
 * @see Reducer Интерфейс, который реализует этот класс.
 * @see PostWallState Состояние, связанное с постами на стене пользователя.
 * @see PostWallMessage Сообщения, которые могут изменять состояние постов.
 * @see PostWallEffect Эффекты, которые выполняются в ответ на сообщения.
 */
class PostWallReducer @Inject constructor() :
    Reducer<PostWallState, PostWallEffect, PostWallMessage> {

    companion object {
        /**
         * Размер страницы для пагинации. Определяет, сколько постов загружается за один запрос.
         */
        const val PAGE_SIZE: Int = 5
    }

    /**
     * Обрабатывает сообщение и возвращает новое состояние и набор эффектов, которые нужно выполнить.
     *
     * @param old Текущее состояние приложения.
     * @param message Сообщение, которое нужно обработать.
     * @return Результат обработки сообщения, содержащий новое состояние и набор эффектов.
     */
    override fun reduce(
        old: PostWallState,
        message: PostWallMessage
    ): ReducerResult<PostWallState, PostWallEffect> =
        when (message) {
            is PostWallMessage.Delete -> ReducerResult(
                newState = old.copy(posts = old.posts.filter { post: PostUiModel ->
                    post.id != message.post.id
                }),

                action = PostWallEffect.Delete(message.post),
            )

            is PostWallMessage.DeleteError -> ReducerResult(
                newState = old.copy(
                    posts = buildList(old.posts.size + 1) {
                        val deletedPost: PostUiModel = message.error.post

                        addAll(old.posts.filter { postInState: PostUiModel ->
                            postInState.id > deletedPost.id
                        })

                        add(deletedPost)

                        addAll(old.posts.filter { postInState: PostUiModel ->
                            postInState.id < deletedPost.id
                        })
                    }
                )
            )

            PostWallMessage.HandleError -> ReducerResult(
                newState = old.copy(
                    singleError = null
                )
            )

            is PostWallMessage.InitialLoaded -> ReducerResult(
                newState = when (
                    val messageResult: Either<Throwable, List<PostUiModel>> = message.result
                ) {
                    is Either.Left -> {
                        if (old.posts.isNotEmpty()) {
                            old.copy(
                                singleError = messageResult.value,
                                statusPost = PostStatus.Idle()
                            )
                        } else {
                            old.copy(
                                statusPost = PostStatus.EmptyError(reason = messageResult.value)
                            )
                        }
                    }

                    is Either.Right -> old.copy(
                        posts = messageResult.value,
                        statusPost = PostStatus.Idle(),
                    )
                }
            )

            is PostWallMessage.Like -> ReducerResult(
                newState = old.copy(
                    posts = old.posts.map { post: PostUiModel ->
                        if (post.id == message.post.id) {
                            post.copy(
                                likedByMe = !post.likedByMe,
                                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                            )
                        } else {
                            post
                        }
                    }
                ),

                action = PostWallEffect.Like(message.post)
            )

            is PostWallMessage.LikeResult -> ReducerResult(
                newState = when (val result = message.result) {
                    is Either.Left -> {
                        val value: PostWithError = result.value
                        val postLiked: PostUiModel = value.post

                        old.copy(
                            posts = old.posts.map { post: PostUiModel ->
                                if (post.id == postLiked.id) {
                                    postLiked
                                } else {
                                    post
                                }
                            },
                            singleError = value.throwable,
                        )
                    }

                    is Either.Right -> {
                        val postLiked = result.value

                        old.copy(
                            posts = old.posts.map { post: PostUiModel ->
                                if (post.id == postLiked.id) {
                                    postLiked
                                } else {
                                    post
                                }
                            },
                        )
                    }
                }
            )

            PostWallMessage.LoadNextPage -> {
                val loadingFinished: Boolean =
                    (old.statusPost as? PostStatus.Idle)?.loadingFinished == true

                val status: PostStatus =
                    if (loadingFinished || old.statusPost !is PostStatus.Idle) {
                        old.statusPost
                    } else {
                        PostStatus.NextPageLoading
                    }

                val effect: PostWallEffect.LoadNextPage? = if (loadingFinished) {
                    null
                } else {
                    PostWallEffect.LoadNextPage(
                        authorId = old.userId,
                        id = old.posts.last().id,
                        count = PAGE_SIZE
                    )
                }

                ReducerResult(
                    newState = old.copy(
                        statusPost = status
                    ),

                    action = effect
                )
            }

            is PostWallMessage.NextPageLoaded -> ReducerResult(
                newState = when (val messageResult = message.result) {
                    is Either.Right -> {
                        val postUiModels: List<PostUiModel> = messageResult.value
                        val loadingFinished: Boolean = postUiModels.size < PAGE_SIZE

                        old.copy(
                            statusPost = PostStatus.Idle(loadingFinished = loadingFinished),
                            posts = old.posts + messageResult.value
                        )
                    }

                    is Either.Left -> {
                        if (BuildConfig.DEBUG) {
                            Logger.e("NextPageLoaded: statusPost = ${PostStatus.NextPageError(reason = messageResult.value)}")
                        }

                        old.copy(
                            statusPost = PostStatus.NextPageError(reason = messageResult.value)
                        )
                    }
                }
            )

            PostWallMessage.Retry -> {
                val nextId: Long? = old.posts.lastOrNull()?.id

                if (nextId == null) {
                    ReducerResult(old)
                } else {
                    ReducerResult(
                        newState = old.copy(
                            statusPost = PostStatus.NextPageLoading,
                        ),

                        action = PostWallEffect.LoadNextPage(
                            authorId = old.userId,
                            id = nextId,
                            count = PAGE_SIZE
                        )
                    )
                }
            }

            PostWallMessage.Refresh -> ReducerResult(
                newState = old.copy(
                    statusPost = if (old.posts.isNotEmpty()) {
                        PostStatus.Refreshing
                    } else {
                        PostStatus.EmptyLoading
                    }
                ),

                action = PostWallEffect.LoadInitialPage(
                    authorId = old.userId,
                    count = PAGE_SIZE
                )
            )
        }
}

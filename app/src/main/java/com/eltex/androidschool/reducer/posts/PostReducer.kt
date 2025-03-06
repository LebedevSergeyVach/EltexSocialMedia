package com.eltex.androidschool.reducer.posts

import arrow.core.Either
import com.eltex.androidschool.BuildConfig

import com.eltex.androidschool.effects.posts.PostEffect
import com.eltex.androidschool.model.posts.PostWithError
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.utils.helper.LoggerHelper
import com.eltex.androidschool.viewmodel.posts.post.PostMessage
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStatus

import javax.inject.Inject

/**
 * Редьюсер для постов. Этот класс реализует интерфейс [Reducer] и отвечает за обработку сообщений,
 * связанных с постами, и обновление состояния приложения.
 *
 * @see Reducer Интерфейс, который реализует этот класс.
 * @see PostState Состояние, связанное с постами на стене пользователя.
 * @see PostMessage Сообщения, которые могут изменять состояние постов.
 * @see PostEffect Эффекты, которые выполняются в ответ на сообщения.
 */
class PostReducer @Inject constructor() : Reducer<PostState, PostEffect, PostMessage> {

    companion object {
        /**
         * Размер страницы для пагинации. Определяет, сколько постов загружается за один запрос.
         */
        const val PAGE_SIZE: Int = 10
    }

    /**
     * Обрабатывает сообщение и возвращает новое состояние и набор эффектов, которые нужно выполнить.
     *
     * @param old Текущее состояние приложения.
     * @param message Сообщение, которое нужно обработать.
     * @return Результат обработки сообщения, содержащий новое состояние и набор эффектов.
     */
    override fun reduce(
        old: PostState,
        message: PostMessage
    ): ReducerResult<PostState, PostEffect> =
        when (message) {
            is PostMessage.Delete -> ReducerResult(
                newState = old.copy(posts = old.posts.filter { post: PostUiModel ->
                    post.id != message.post.id
                }),

                action = PostEffect.Delete(message.post),
            )

            is PostMessage.DeleteError -> ReducerResult(
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

            PostMessage.HandleError -> ReducerResult(
                newState = old.copy(
                    singleError = null
                )
            )

            is PostMessage.InitialLoaded -> ReducerResult(
                newState = when (
                    val messageResult: Either<Throwable, List<PostUiModel>> = message.result
                ) {
                    is Either.Left -> {
                        if (old.posts.isNotEmpty()) {
                            old.copy(
                                singleError = messageResult.value,
                                statusPost = PostStatus.Idle(),
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

            is PostMessage.Like -> ReducerResult(
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

                action = PostEffect.Like(message.post)
            )

            is PostMessage.LikeResult -> ReducerResult(
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
                        val postLiked: PostUiModel = result.value

                        old.copy(
                            posts = old.posts.map { post: PostUiModel ->
                                if (post.id == postLiked.id) {
                                    postLiked
                                } else {
                                    post
                                }
                            }
                        )
                    }
                }
            )

            PostMessage.LoadNextPage -> {
                val loadingFinished: Boolean =
                    (old.statusPost as? PostStatus.Idle)?.loadingFinished == true

                val status: PostStatus =
                    if (loadingFinished || old.statusPost !is PostStatus.Idle) {
                        old.statusPost
                    } else {
                        PostStatus.NextPageLoading
                    }

                val effect: PostEffect.LoadNextPage? = if (loadingFinished) {
                    null
                } else {
                    PostEffect.LoadNextPage(
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

            is PostMessage.NextPageLoaded -> ReducerResult(
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
                            LoggerHelper.e("NextPageLoaded: statusPost = ${PostStatus.NextPageError(reason = messageResult.value)}")
                        }

                        old.copy(
                            statusPost = PostStatus.NextPageError(reason = messageResult.value)
                        )
                    }
                }
            )

            PostMessage.Retry -> {
                val nextId: Long? = old.posts.lastOrNull()?.id

                if (nextId == null) {
                    ReducerResult(old)
                } else {
                    ReducerResult(
                        newState = old.copy(
                            statusPost = PostStatus.NextPageLoading,
                        ),

                        action = PostEffect.LoadNextPage(
                            id = nextId,
                            count = PAGE_SIZE
                        )
                    )
                }
            }

            PostMessage.Refresh -> ReducerResult(
                newState = old.copy(
                    statusPost = if (old.posts.isNotEmpty()) {
                        PostStatus.Refreshing
                    } else {
                        PostStatus.EmptyLoading
                    }
                ),

                action = PostEffect.LoadInitialPage(count = PAGE_SIZE)
            )
        }
}

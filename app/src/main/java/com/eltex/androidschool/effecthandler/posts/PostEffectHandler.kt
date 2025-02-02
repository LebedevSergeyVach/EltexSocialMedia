package com.eltex.androidschool.effecthandler.posts

import arrow.core.Either
import arrow.core.left
import arrow.core.right

import com.eltex.androidschool.effects.posts.PostEffect
import com.eltex.androidschool.model.posts.PostWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import com.eltex.androidschool.viewmodel.posts.post.PostMessage

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

import javax.inject.Inject

/**
 * Обработчик эффектов для постов. Этот класс реализует интерфейс [EffectHandler] и отвечает за
 * обработку различных эффектов, таких как загрузка постов, лайки и удаление.
 *
 * @param repository Репозиторий для работы с постами в сети.
 * @param mapper Маппер для преобразования моделей данных в UI-модели.
 */
class PostEffectHandler @Inject constructor(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
) : EffectHandler<PostEffect, PostMessage> {

    /**
     * Подключает поток эффектов к потоку сообщений. Этот метод объединяет обработчики для всех типов эффектов.
     *
     * @param effects Поток эффектов, которые нужно обработать.
     * @return Поток сообщений, которые будут отправлены в хранилище (Store).
     */
    override fun connect(effects: Flow<PostEffect>): Flow<PostMessage> =
        listOf(
            handleNextPage(effects = effects),
            handleInitialPage(effects = effects),
            handleLikePost(effects = effects),
            handleDeletePost(effects = effects),
        )
            .merge()

    /**
     * Обрабатывает эффект загрузки следующей страницы постов.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostEffect.LoadNextPage].
     * @return Поток сообщений, содержащий результат загрузки следующей страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleNextPage(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.LoadNextPage>()
            .mapLatest { postEffect: PostEffect.LoadNextPage ->
                PostMessage.NextPageLoaded(
                    try {
                        repository.getBeforePosts(
                            id = postEffect.id,
                            count = postEffect.count
                        )
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    /**
     * Обрабатывает эффект загрузки начальной страницы постов.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostEffect.LoadInitialPage].
     * @return Поток сообщений, содержащий результат загрузки начальной страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleInitialPage(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.LoadInitialPage>()
            .mapLatest { postEffect: PostEffect.LoadInitialPage ->
                PostMessage.InitialLoaded(
                    try {
                        repository.getLatestPosts(
                            count = postEffect.count
                        )
                            .map(mapper::map)
                            .right()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        e.left()
                    }
                )
            }

    /**
     * Обрабатывает эффект лайка поста.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostEffect.Like].
     * @return Поток сообщений, содержащий результат лайка поста.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleLikePost(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.Like>()
            .mapLatest { postEffect: PostEffect.Like ->
                PostMessage.LikeResult(
                    try {
                        Either.Right(
                            mapper.map(
                                repository.likeById(
                                    postId = postEffect.post.id,
                                    likedByMe = postEffect.post.likedByMe
                                )
                            )
                        )
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        Either.Left(PostWithError(post = postEffect.post, throwable = e))
                    }
                )
            }

    /**
     * Обрабатывает эффект удаления поста.
     *
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostEffect.Delete].
     * @return Поток сообщений, содержащий ошибку удаления, если она произошла.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleDeletePost(effects: Flow<PostEffect>) =
        effects.filterIsInstance<PostEffect.Delete>()
            .mapLatest { postEffect: PostEffect.Delete ->
                try {
                    repository.deleteById(postId = postEffect.post.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    PostMessage.DeleteError(
                        PostWithError(
                            post = postEffect.post, throwable = e
                        )
                    )
                }
            }
            .filterIsInstance<PostMessage.DeleteError>()
}

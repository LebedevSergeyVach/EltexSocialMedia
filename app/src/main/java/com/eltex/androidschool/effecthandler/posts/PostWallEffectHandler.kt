package com.eltex.androidschool.effecthandler.posts

import arrow.core.Either
import arrow.core.left
import arrow.core.right

import com.eltex.androidschool.effects.posts.PostWallEffect
import com.eltex.androidschool.model.posts.PostWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

/**
 * Обработчик эффектов для постов на стене пользователя. Этот класс реализует интерфейс [EffectHandler] и отвечает за
 * обработку различных эффектов, таких как загрузка постов, лайки и удаление.
 *
 * @param repository Репозиторий для работы с постами в сети.
 * @param mapper Маппер для преобразования моделей данных в UI-модели.
 */
class PostWallEffectHandler(
    private val repository: PostRepository,
    private val mapper: PostUiModelMapper,
) : EffectHandler<PostWallEffect, PostWallMessage> {

    /**
     * Подключает поток эффектов к потоку сообщений. Этот метод объединяет обработчики для всех типов эффектов.
     *
     * @param effects Поток эффектов, которые нужно обработать.
     * @return Поток сообщений, которые будут отправлены в хранилище (Store).
     */
    override fun connect(effects: Flow<PostWallEffect>): Flow<PostWallMessage> =
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
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostWallEffect.LoadNextPage].
     * @return Поток сообщений, содержащий результат загрузки следующей страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleNextPage(effects: Flow<PostWallEffect>) =
        effects.filterIsInstance<PostWallEffect.LoadNextPage>()
            .mapLatest { postEffect: PostWallEffect.LoadNextPage ->
                PostWallMessage.NextPageLoaded(
                    try {
                        repository.getBeforePostsByAuthorId(
                            authorId = postEffect.authorId,
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
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostWallEffect.LoadInitialPage].
     * @return Поток сообщений, содержащий результат загрузки начальной страницы.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleInitialPage(effects: Flow<PostWallEffect>) =
        effects.filterIsInstance<PostWallEffect.LoadInitialPage>()
            .mapLatest { postEffect: PostWallEffect.LoadInitialPage ->
                PostWallMessage.InitialLoaded(
                    try {
                        repository.getLatestPostsByAuthorId(
                            authorId = postEffect.authorId,
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
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostWallEffect.Like].
     * @return Поток сообщений, содержащий результат лайка поста.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleLikePost(effects: Flow<PostWallEffect>) =
        effects.filterIsInstance<PostWallEffect.Like>()
            .mapLatest { postEffect: PostWallEffect.Like ->
                PostWallMessage.LikeResult(
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
     * @param effects Поток эффектов, из которого фильтруются эффекты типа [PostWallEffect.Delete].
     * @return Поток сообщений, содержащий ошибку удаления, если она произошла.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleDeletePost(effects: Flow<PostWallEffect>) =
        effects.filterIsInstance<PostWallEffect.Delete>()
            .mapLatest { postEffect: PostWallEffect.Delete ->
                try {
                    repository.deleteById(postId = postEffect.post.id)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    PostWallMessage.DeleteError(PostWithError(post = postEffect.post, throwable = e))
                }
            }
            .filterIsInstance<PostWallMessage.DeleteError>()
}

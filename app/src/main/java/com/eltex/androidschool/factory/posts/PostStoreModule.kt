package com.eltex.androidschool.factory.posts

import com.eltex.androidschool.effecthandler.posts.PostEffectHandler
import com.eltex.androidschool.effecthandler.posts.PostWallEffectHandler
import com.eltex.androidschool.reducer.posts.PostReducer
import com.eltex.androidschool.reducer.posts.PostWallReducer
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.viewmodel.posts.post.PostMessage
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStore
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallMessage
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallStore

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Фабрика для создания хранилищ (Store) связанных с постами.
 * Этот класс отвечает за создание экземпляров [PostStore] и [PostWallStore],
 * которые управляют состоянием и логикой для отображения постов и стены постов.
 *
 * @property repository Репозиторий для работы с данными постов.
 *
 * @see PostStore
 * @see PostWallStore
 * @see PostRepository
 */
@Module
@InstallIn(ViewModelComponent::class)
class PostStoreModule {

    /**
     * Создает экземпляр [PostStore], который управляет состоянием и логикой для отображения одного поста.
     *
     * @return [PostStore] Экземпляр хранилища для управления состоянием поста.
     *
     * @see PostStore
     * @see PostReducer
     * @see PostEffectHandler
     * @see PostMessage
     * @see PostState
     */
    @Provides
    fun createPostFactory(
        reducer: PostReducer,
        effectHandler: PostEffectHandler,
    ): PostStore = PostStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(PostMessage.Refresh),
        initState = PostState(),
    )

    /**
     * Создает экземпляр [PostWallStore], который управляет состоянием и логикой для отображения стены постов.
     *
     * @param userId Идентификатор пользователя, для которого создается стена постов.
     * @return [PostWallStore] Экземпляр хранилища для управления состоянием стены постов.
     *
     * @see PostWallStore
     * @see PostWallReducer
     * @see PostWallEffectHandler
     * @see PostWallMessage
     * @see PostWallState
     */
    @Provides
    fun createPostsWallFactory(
        reducer: PostWallReducer,
        effectHandler: PostWallEffectHandler,
    ): PostWallStore = PostWallStore(
        reducer = reducer,
        effectHandler = effectHandler,
        initMessages = setOf(PostWallMessage.Refresh),
        initState = PostWallState(),
    )
}

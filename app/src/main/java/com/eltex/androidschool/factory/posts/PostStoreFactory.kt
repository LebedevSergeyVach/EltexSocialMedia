package com.eltex.androidschool.factory.posts

import com.eltex.androidschool.effecthandler.posts.PostEffectHandler
import com.eltex.androidschool.reducer.posts.PostReducer
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import com.eltex.androidschool.viewmodel.posts.post.PostMessage
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStore

class PostStoreFactory(
    private val repository: PostRepository
) {
    fun createPostFactory(): PostStore = PostStore(
        reducer = PostReducer(),
        effectHandler = PostEffectHandler(
            repository = repository,
            mapper = PostUiModelMapper()
        ),
        initMessages = setOf(PostMessage.Refresh),
        initState = PostState(),
    )
}

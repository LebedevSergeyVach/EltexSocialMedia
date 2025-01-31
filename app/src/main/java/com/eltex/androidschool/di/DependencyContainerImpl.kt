package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory

import com.eltex.androidschool.api.common.OkHttpClientFactory
import com.eltex.androidschool.api.common.RetrofitFactory
import com.eltex.androidschool.api.media.MediaApi
import com.eltex.androidschool.api.posts.PostsApi
import com.eltex.androidschool.factory.posts.PostStoreFactory
import com.eltex.androidschool.repository.posts.NetworkPostRepository
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.viewmodel.posts.newposts.NewPostViewModel
import com.eltex.androidschool.viewmodel.posts.post.PostViewModel
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.create

class DependencyContainerImpl : DependencyContainer {

    private val okHttpClient: OkHttpClient = OkHttpClientFactory.createOkHttpClient()
    private val retrofit: Retrofit = RetrofitFactory.createRetrofit(okHttpClient)
    private val postsApi: PostsApi = retrofit.create()
    private val mediaApi: MediaApi = retrofit.create()

    private val postRepository: PostRepository = NetworkPostRepository(
        postsApi = postsApi,
        mediaApi = mediaApi,
    )

    private val postStoreFactory = PostStoreFactory(repository = postRepository)

    override fun getPostsViewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    postStore = postStoreFactory.createPostFactory()
                )
            }
        }

    override fun getNewPostViewModelFactory(postId: Long): ViewModelProvider.Factory =
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    repository = postRepository,
                    postId = postId,
                )
            }
        }
}

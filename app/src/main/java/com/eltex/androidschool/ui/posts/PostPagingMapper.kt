package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStatus
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState

object PostPagingMapper {
    fun map(state: PostState): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = state.posts.map { post: PostUiModel ->
            PagingModel.Data(post)
        }

        return when (val statusValue = state.statusPost) {
            PostStatus.NextPageLoading -> posts + PagingModel.Loading
            is PostStatus.NextPageError -> posts + PagingModel.Error(reason = statusValue.reason)
            is PostStatus.Idle,
            PostStatus.Refreshing,
            is PostStatus.EmptyError,
            PostStatus.EmptyLoading -> posts
        }
    }

    fun map(state: PostWallState): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = state.posts.map { post: PostUiModel ->
            PagingModel.Data(post)
        }

        return when (val statusValue = state.statusPost) {
            PostStatus.NextPageLoading -> posts + PagingModel.Loading
            is PostStatus.NextPageError -> posts + PagingModel.Error(reason = statusValue.reason)
            is PostStatus.Idle,
            PostStatus.Refreshing,
            is PostStatus.EmptyError,
            PostStatus.EmptyLoading -> posts
        }
    }
}

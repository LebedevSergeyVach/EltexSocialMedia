package com.eltex.androidschool.ui.posts

import com.eltex.androidschool.reducer.posts.PostReducer
import com.eltex.androidschool.reducer.posts.PostWallReducer
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStatus
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState

/**
 * Маппер для преобразования состояния постов в модель пагинации.
 * Используется для отображения постов, ошибок и состояния загрузки в RecyclerView.
 */
object PostPagingMapper {

    /**
     * Преобразует состояние постов в список моделей пагинации.
     *
     * @param state Состояние постов.
     * @return Список моделей пагинации.
     */
    fun map(state: PostState): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = state.posts.map { post: PostUiModel ->
            PagingModel.Data(post)
        }

        return when (val statusValue = state.statusPost) {
            PostStatus.EmptyLoading -> List(PostReducer.PAGE_SIZE) { PagingModel.Loading }
            PostStatus.NextPageLoading -> posts + List(PostReducer.PAGE_SIZE) { PagingModel.Loading }
            is PostStatus.NextPageError -> posts + PagingModel.Error(reason = statusValue.reason)
            PostStatus.Refreshing,
            is PostStatus.Idle,
            is PostStatus.EmptyError -> posts
        }
    }

    /**
     * Преобразует состояние стены постов в список моделей пагинации.
     *
     * @param state Состояние стены постов.
     * @return Список моделей пагинации.
     */
    fun map(state: PostWallState): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = state.posts.map { post: PostUiModel ->
            PagingModel.Data(post)
        }

        return when (val statusValue = state.statusPost) {
            PostStatus.EmptyLoading -> List(PostWallReducer.PAGE_SIZE) { PagingModel.Loading }
            PostStatus.NextPageLoading -> posts + List(PostWallReducer.PAGE_SIZE) { PagingModel.Loading }
            is PostStatus.NextPageError -> posts + PagingModel.Error(reason = statusValue.reason)
            PostStatus.Refreshing,
            is PostStatus.Idle,
            is PostStatus.EmptyError -> posts
        }
    }
}

package com.eltex.androidschool.ui.posts

import android.content.Context

import com.eltex.androidschool.R
import com.eltex.androidschool.reducer.posts.PostReducer
import com.eltex.androidschool.reducer.posts.PostWallReducer
import com.eltex.androidschool.ui.common.PagingModel
import com.eltex.androidschool.viewmodel.posts.post.PostState
import com.eltex.androidschool.viewmodel.posts.post.PostStatus
import com.eltex.androidschool.viewmodel.posts.postswall.PostWallState

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Маппер для преобразования состояния постов в модель пагинации.
 * Используется для отображения постов, ошибок и состояния загрузки в RecyclerView.
 */
object PostPagingMapper {

    /**
     * Преобразует состояние постов в список моделей пагинации.
     *
     * @param state Состояние постов.
     * @param context Контекст для доступа к строковым ресурсам.
     * @return Список моделей пагинации.
     */
    fun map(state: PostState, context: Context): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = state.posts.map { post: PostUiModel ->
            PagingModel.Data(post)
        }

        val groupedPosts = mutableListOf<PagingModel<PostUiModel>>()
        var lastDate: String? = null

        for (post in posts) {
            val postDate = getFormattedDate(date = post.value.published, context = context)

            if (postDate != lastDate) {
                groupedPosts.add(PagingModel.DateSeparator(postDate))
                lastDate = postDate
            }

            groupedPosts.add(post)
        }

        return when (val statusValue = state.statusPost) {
            PostStatus.EmptyLoading -> List(PostReducer.PAGE_SIZE) { PagingModel.Loading }
            PostStatus.NextPageLoading -> groupedPosts + List(PostReducer.PAGE_SIZE) { PagingModel.Loading }
            is PostStatus.NextPageError -> groupedPosts + PagingModel.Error(reason = statusValue.reason)
            PostStatus.Refreshing, is PostStatus.Idle, is PostStatus.EmptyError -> groupedPosts
        }
    }

    /**
     * Преобразует состояние стены постов в список моделей пагинации.
     *
     * @param state Состояние стены постов.
     * @param context Контекст для доступа к строковым ресурсам.
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

    /**
     * Форматирует дату в строку с использованием строковых ресурсов.
     * Месяц отображается текстом, а не числом.
     *
     * @param date Дата в формате строки.
     * @param context Контекст для доступа к строковым ресурсам.
     * @return Отформатированная строка с датой.
     */
    @Suppress("DEPRECATION")
    private fun getFormattedDate(date: String, context: Context): String {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val postDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"))

        return when (postDate) {
            today -> context.getString(R.string.today)
            yesterday -> context.getString(R.string.yesterday)

            else -> {
                val formatter = DateTimeFormatter.ofPattern(
                    "dd MMM yyyy",
                    context.resources.configuration.locale
                )
                postDate.format(formatter)
            }
        }
    }
}

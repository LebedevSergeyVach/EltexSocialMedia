package com.eltex.androidschool.viewmodel.comments

import com.eltex.androidschool.ui.comments.CommentUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

data class CommentsState(
    val comments: List<CommentUiModel>? = null,
    val statusComments: StatusLoad = StatusLoad.Idle,
    val isButtonEnabled: Boolean = false,
) {

    val isRefreshing: Boolean
        get() = statusComments == StatusLoad.Loading && comments?.isNotEmpty() == true

    val isEmptyLoading: Boolean
        get() = statusComments == StatusLoad.Loading && comments.isNullOrEmpty()

    val isRefreshError: Boolean
        get() = statusComments is StatusLoad.Error && comments?.isNotEmpty() == true

    val isEmptyError: Boolean
        get() = statusComments is StatusLoad.Error && comments.isNullOrEmpty()

    val isEmptySuccess: Boolean
        get() = statusComments is StatusLoad.Success && comments.isNullOrEmpty()

    val isEmptyIdle: Boolean
        get() = statusComments is StatusLoad.Idle && comments.isNullOrEmpty()

    val isFirstSecaucus: Boolean
        get() = statusComments is StatusLoad.Success && !comments.isNullOrEmpty()
}

package com.eltex.androidschool.viewmodel.posts.postdetails

import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.viewmodel.status.StatusLoad

data class PostDetailsState(
    val post: PostUiModel? = null,
    val statusLoadPost: StatusLoad = StatusLoad.Idle,
) {

    val isEmptyLoading: Boolean
        get() = statusLoadPost is StatusLoad.Loading && post == null

    val isEmptyError: Boolean
        get() = statusLoadPost is StatusLoad.Error && post == null

    val isIdlePost: Boolean
        get() = statusLoadPost is StatusLoad.Idle && post != null
}

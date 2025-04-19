package com.eltex.androidschool.viewmodel.posts.postdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.data.posts.PostData
import com.eltex.androidschool.repository.posts.PostRepository
import com.eltex.androidschool.ui.common.DateTimeUiFormatter
import com.eltex.androidschool.ui.posts.PostUiModel
import com.eltex.androidschool.ui.posts.PostUiModelMapper
import com.eltex.androidschool.viewmodel.status.StatusLoad
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId

@HiltViewModel(assistedFactory = PostDetailsViewModel.ViewModelFactory::class)
class PostDetailsViewModel @AssistedInject constructor(
    private val repository: PostRepository,
    @Assisted private val postId: Long,
) : ViewModel() {
    private val _state: MutableStateFlow<PostDetailsState> = MutableStateFlow(PostDetailsState())
    val state: StateFlow<PostDetailsState> = _state.asStateFlow()

    private val mapper = PostUiModelMapper(
        dateTimeUiFormatter = DateTimeUiFormatter(
            zoneId = ZoneId.systemDefault()
        )
    )

    init {
        loadPost(postId = postId)
    }

    fun loadPost(postId: Long) {
        _state.update { statePostDetails: PostDetailsState ->
            statePostDetails.copy(
                statusLoadPost = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val post: PostData = repository.getPostById(postId = postId)

                val postUiModel: PostUiModel = mapper.map(post = post, mapListAvatarModel = true)

                _state.update { statePostDetails: PostDetailsState ->
                    statePostDetails.copy(
                        statusLoadPost = StatusLoad.Idle,
                        post = postUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { statePostDetails: PostDetailsState ->
                    statePostDetails.copy(
                        statusLoadPost = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }


    fun likeById(postId: Long, likedByMe: Boolean) {
        _state.update { statePostDetails: PostDetailsState ->
            statePostDetails.copy(
                statusLoadPost = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val post: PostData = repository.likeById(
                    postId = postId,
                    likedByMe = likedByMe,
                )

                val postUiModel: PostUiModel = mapper.map(post = post, mapListAvatarModel = true)

                _state.update { statePostDetails: PostDetailsState ->
                    statePostDetails.copy(
                        statusLoadPost = StatusLoad.Idle,
                        post = postUiModel,
                    )
                }
            } catch (e: Exception) {
                _state.update { statePostDetails: PostDetailsState ->
                    statePostDetails.copy(
                        statusLoadPost = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun consumerError() {
        _state.update { statePostDetails: PostDetailsState ->
            statePostDetails.copy(
                statusLoadPost = StatusLoad.Idle,
            )
        }
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(postId: Long): PostDetailsViewModel
    }
}

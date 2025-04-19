package com.eltex.androidschool.viewmodel.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.data.comments.CommentData
import com.eltex.androidschool.repository.comments.CommentRepository
import com.eltex.androidschool.ui.comments.CommentUiModel
import com.eltex.androidschool.ui.comments.CommentUiModelMapper
import com.eltex.androidschool.ui.common.DateTimeUiFormatter
import com.eltex.androidschool.viewmodel.status.StatusLoad

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.time.ZoneId

@HiltViewModel(assistedFactory = CommentsViewModel.ViewModelFactory::class)
class CommentsViewModel @AssistedInject constructor(
    private val repository: CommentRepository,
    @Assisted private val postIdFragment: Long,
) : ViewModel() {

    private val _state: MutableStateFlow<CommentsState> = MutableStateFlow(CommentsState())
    val state: StateFlow<CommentsState> = _state.asStateFlow()

    private val mapper = CommentUiModelMapper(
        dateTimeUiFormatter = DateTimeUiFormatter(zoneId = ZoneId.systemDefault())
    )

    init {
        load(postId = postIdFragment)
    }

    fun load(postId: Long) {
        getAllCommentsForPostById(postId = postId)
    }

    private fun getAllCommentsForPostById(postId: Long) {
        _state.update { stateComments: CommentsState ->
            stateComments.copy(
                statusComments = StatusLoad.Loading,
            )
        }

        viewModelScope.launch {
            try {
                val comments: List<CommentData> =
                    repository.getAllCommentsForPostById(postId = postId)

                val commentsUiModels: List<CommentUiModel> = withContext(Dispatchers.Default) {
                    comments.map { comment: CommentData ->
                        mapper.map(comment = comment)
                    }
                }

                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        comments = commentsUiModels,
                        statusComments = StatusLoad.Success,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        statusComments = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun likeCommentById(postId: Long, commentId: Long, likedByMe: Boolean) {
        viewModelScope.launch {
            try {
                val comment: CommentData = repository.likeCommentForPostById(
                    postId = postId,
                    commentId = commentId,
                    likedByMe = likedByMe,
                )

                val commentsUiModel: List<CommentUiModel> = withContext(Dispatchers.Default) {
                    _state.value.comments.orEmpty().map { commentUiModel: CommentUiModel ->
                        if (commentUiModel.id == comment.id) {
                            mapper.map(comment)
                        } else {
                            commentUiModel
                        }
                    }
                }

                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        comments = commentsUiModel,
                        statusComments = StatusLoad.Idle,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        statusComments = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun deleteCommentById(postId: Long, commentId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteCommentForPostById(postId = postId, commentId = commentId)

                _state.update { stateComment: CommentsState ->
                    stateComment.copy(
                        comments = _state.value.comments.orEmpty()
                            .filter { comment: CommentUiModel ->
                                comment.id != commentId
                            },
                        statusComments = StatusLoad.Idle,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        statusComments = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun sendCommentByIdPost(postId: Long, content: String) {
        viewModelScope.launch {
            try {
                val comment: CommentData = repository.saveCommentForPostById(
                    postId = postId,
                    content = content
                )

                val commentUiModel: CommentUiModel = mapper.map(comment)

                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        comments = _state.value.comments.orEmpty().toMutableList().apply {
                            add(commentUiModel)
                        },
                        statusComments = StatusLoad.Idle,
                    )
                }
            } catch (e: Exception) {
                _state.update { stateComments: CommentsState ->
                    stateComments.copy(
                        statusComments = StatusLoad.Error(exception = e),
                    )
                }
            }
        }
    }

    fun updateButtonState(comment: String) {
        val isButtonEnabled: Boolean = comment.isNotBlank()

        _state.update { stateComments: CommentsState ->
            stateComments.copy(
                isButtonEnabled = isButtonEnabled,
            )
        }
    }

    fun consumerError() {
        _state.update { stateComments: CommentsState ->
            stateComments.copy(
                statusComments = StatusLoad.Idle,
            )
        }
    }

    @AssistedFactory
    interface ViewModelFactory {
        fun create(postId: Long): CommentsViewModel
    }
}

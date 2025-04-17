package com.eltex.androidschool.repository.comments

import com.eltex.androidschool.api.comments.CommentsApi
import com.eltex.androidschool.data.comments.CommentData

import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentsApi: CommentsApi
) : CommentRepository {
    override suspend fun getAllCommentsForPostById(postId: Long): List<CommentData> =
        commentsApi.getAllCommentsForPostById(postId = postId)

    override suspend fun saveCommentForPostById(
        postId: Long,
        content: String
    ): CommentData =
        commentsApi.saveCommentForPostById(
            postId = postId,
            comment = CommentData(
                content = content,
            )
        )

    override suspend fun likeCommentForPostById(
        postId: Long,
        commentId: Long,
        likedByMe: Boolean
    ): CommentData {
        return if (likedByMe) {
            commentsApi.unlikeCommentForPostById(postId = postId, commentId = commentId)
        } else {
            commentsApi.likeCommentForPostById(postId = postId, commentId = commentId)
        }
    }

    override suspend fun deleteCommentForPostById(postId: Long, commentId: Long) =
        commentsApi.deleteCommentForPostById(postId = postId, commentId = commentId)
}

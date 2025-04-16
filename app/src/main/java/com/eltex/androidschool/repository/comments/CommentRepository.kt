package com.eltex.androidschool.repository.comments

import com.eltex.androidschool.data.comments.CommentData

interface CommentRepository {
    suspend fun getAllCommentsForPostById(postId: Long): List<CommentData>
    suspend fun saveCommentForPostById(
        postId: Long,
        content: String,
    ): CommentData
    suspend fun likeCommentForPostById(postId: Long, commentId: Long, likedByMe: Boolean): CommentData
    suspend fun deleteCommentForPostById(postId: Long, commentId: Long)
}

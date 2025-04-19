package com.eltex.androidschool.api.comments

import com.eltex.androidschool.data.comments.CommentData

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentsApi {
    @GET("api/posts/{postId}/comments")
    suspend fun getAllCommentsForPostById(@Path("postId") postId: Long): List<CommentData>

    @POST("api/posts/{postId}/comments")
    suspend fun saveCommentForPostById(
        @Path("postId") postId: Long,
        @Body comment: CommentData
    ): CommentData

    @POST("api/posts/{postId}/comments/{id}/likes")
    suspend fun likeCommentForPostById(
        @Path("postId") postId: Long,
        @Path("id") commentId: Long
    ): CommentData

    @DELETE("api/posts/{postId}/comments/{id}/likes")
    suspend fun unlikeCommentForPostById(
        @Path("postId") postId: Long,
        @Path("id") commentId: Long
    ): CommentData

    @DELETE("api/posts/{postId}/comments/{id}")
    suspend fun deleteCommentForPostById(
        @Path("postId") postId: Long,
        @Path("id") commentId: Long
    )
}

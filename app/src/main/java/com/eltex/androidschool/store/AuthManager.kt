package com.eltex.androidschool.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val userPreferences: UserPreferences
) {
    val authTokenFlow: Flow<String?> = userPreferences.authTokenFlow
    val userIdFlow: Flow<String?> = userPreferences.userIdFlow

    suspend fun getAuthToken(): String? {
        return userPreferences.authTokenFlow.first()
    }

    suspend fun getUserId(): Long? {
        return userPreferences.userIdFlow.first()?.toLong()
    }
}

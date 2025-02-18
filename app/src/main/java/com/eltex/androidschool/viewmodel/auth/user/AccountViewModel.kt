package com.eltex.androidschool.viewmodel.auth.user

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.store.UserPreferences

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import okio.IOException

import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private val _userId = runBlocking {
        userPreferences.userIdFlow.first()?.toLong()
    } ?: throw IOException("USER ID NOT FOUND")

    val userId: Long = _userId
}

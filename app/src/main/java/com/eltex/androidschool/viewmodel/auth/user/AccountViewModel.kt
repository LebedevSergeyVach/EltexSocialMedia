package com.eltex.androidschool.viewmodel.auth.user

import androidx.lifecycle.ViewModel

import com.eltex.androidschool.store.UserPreferences

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private val _userId: Long = runBlocking {
        userPreferences.userIdFlow.first()
    }
        ?: 3L

    val userId: Long = _userId
}

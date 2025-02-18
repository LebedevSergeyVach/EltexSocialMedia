package com.eltex.androidschool.viewmodel.auth.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.eltex.androidschool.store.UserPreferences

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class AuthorizationSharedViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _isAuthorized = MutableStateFlow(true)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            try {
                userPreferences.authTokenFlow.collect { authToken ->
                    _isAuthorized.value = authToken != null
                }
            } catch (e: Exception) {
                _isAuthorized.value = false
            }
        }
    }
}

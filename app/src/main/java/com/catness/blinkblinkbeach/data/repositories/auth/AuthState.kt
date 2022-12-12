package com.catness.blinkblinkbeach.data.repositories.auth

sealed class AuthState {
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String? = null) : AuthState()
}

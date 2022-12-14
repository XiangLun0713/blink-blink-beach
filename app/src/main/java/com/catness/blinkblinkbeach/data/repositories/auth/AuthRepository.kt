package com.catness.blinkblinkbeach.data.repositories.auth

interface AuthRepository {
    suspend fun signOut()
    suspend fun sendResetPasswordEmail(email: String): String?
    suspend fun signUp(username: String, email: String, password: String): AuthState
    suspend fun signIn(email: String, password: String): AuthState
}
package com.catness.blinkblinkbeach.data.repositories.auth

interface AuthRepository {
    suspend fun signOut()
    suspend fun register(email: String, password: String): AuthState
    suspend fun signIn(email: String, password: String): AuthState
}
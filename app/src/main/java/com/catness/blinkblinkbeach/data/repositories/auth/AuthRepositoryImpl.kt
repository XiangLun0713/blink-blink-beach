package com.catness.blinkblinkbeach.data.repositories.auth

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun register(email: String, password: String): AuthState {
        if (email.isEmpty() || password.isEmpty()) return AuthState.Error("Register failed: Please ensure that you've entered both the email and password.")
        if (!isValidEmailAddress(email.trim())) return AuthState.Error("Register failed: Invalid email address.")

        return try {
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            AuthState.Success
        } catch (e: Exception) {
            // register fails
            AuthState.Error("Register failed: ${e.message}")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthState {
        if (email.isEmpty() || password.isEmpty()) return AuthState.Error("Sign in failed: Please ensure that you've entered both the email and password.")
        if (!isValidEmailAddress(email.trim())) return AuthState.Error("Sign in failed: Invalid email address.")

        return try {
            firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            AuthState.Success
        } catch (e: Exception) {
            // sign in fails
            AuthState.Error("Sign in failed: ${e.message}")
        }
    }

    private fun isValidEmailAddress(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
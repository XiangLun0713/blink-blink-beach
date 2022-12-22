package com.catness.blinkblinkbeach.data.repositories.auth

import android.util.Patterns
import com.catness.blinkblinkbeach.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun sendResetPasswordEmail(email: String): String? {
        if (email.isEmpty()) return "Reset password failed: Please ensure that you've entered your email address."
        if (!isValidEmailAddress(email.trim())) return "Reset password failed: Invalid email address."

        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            null
        } catch (e: Exception) {
            "Reset password failed: ${e.message}"
        }
    }

    override suspend fun signUp(username: String, email: String, password: String): AuthState {
        if (email.isEmpty() || password.isEmpty() || username.isBlank()) return AuthState.Error("Sign up failed: Please ensure that you've entered all the required information.")
        if (!isValidEmailAddress(email.trim())) return AuthState.Error("Sign up failed: Invalid email address.")

        return try {
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            if (firebaseAuth.currentUser != null) {
                val uid = firebaseAuth.currentUser!!.uid
                firestore.collection("users").document(uid)
                    .set(User(uid, username.trim(), email, "", emptyList())).await()
            }
            AuthState.Success
        } catch (e: Exception) {
            // sign up fails
            AuthState.Error("Sign up failed: ${e.message}")
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
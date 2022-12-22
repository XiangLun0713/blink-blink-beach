package com.catness.blinkblinkbeach.data.repositories.profile

import android.util.Log
import com.catness.blinkblinkbeach.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ProfileRepositoryImpl"

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ProfileRepository {

    override suspend fun getCurrentUser(): User {
        return try {
            val snapshot =
                firestore.collection("users").document(firebaseAuth.uid.toString()).get().await()
            val user = snapshot.toObject(User::class.java) ?: throw Exception()
            user
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentUser: ${e.message}")
            User()
        }
    }
}
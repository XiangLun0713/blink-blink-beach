package com.catness.blinkblinkbeach.data.repositories.profile

import android.net.Uri
import android.util.Log
import com.catness.blinkblinkbeach.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) : ProfileRepository {

    override suspend fun getCurrentUser(): User {
        return try {
            val snapshot =
                firestore.collection("users").document(firebaseAuth.uid.toString()).get().await()
            val user = snapshot.toObject(User::class.java) ?: throw Exception()
            user
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", "getCurrentUser: ${e.message}")
            User()
        }
    }

    override suspend fun uploadImageAndReturnUser(uri: Uri): User {
        return try {
            // store image to firebase storage
            val childRef = storageReference.child("user/${uri.lastPathSegment}")
            val uploadTask = childRef.putFile(uri).await()
            val imageUrl = uploadTask.metadata?.reference?.downloadUrl?.await() ?: throw Exception()

            // get and update current user information from database
            val snapshot =
                firestore.collection("users").document(firebaseAuth.uid.toString()).get().await()
            val user = snapshot.toObject(User::class.java) ?: throw Exception()
            val newUser = user.copy(profileImageUrl = imageUrl.toString())

            // update the user information in firestore
            firestore.collection("users").document(firebaseAuth.uid.toString()).set(newUser).await()

            // remove the previous image
            val imageRef = firebaseStorage.getReferenceFromUrl(user.profileImageUrl)
            imageRef.delete().await()

            newUser
        } catch (e: Exception) {
            // return error state
            println("Upload image failed: ${e.message}")
            User()
        }
    }

    override suspend fun updateUsername(username: String): User {
        return try {
            if (username.trim().isEmpty()) throw Exception("Please enter the username.")
            val snapshot =
                firestore.collection("users").document(firebaseAuth.uid.toString()).get().await()
            val user = snapshot.toObject(User::class.java) ?: throw Exception()
            return if (username.trim() != user.username) {
                val newUser = user.copy(username = username.trim())
                firestore.collection("users").document(firebaseAuth.uid.toString()).set(newUser)
                    .await()
                newUser
            } else {
                user
            }
        } catch (e: Exception) {
            User()
        }
    }
}
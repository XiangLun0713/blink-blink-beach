package com.catness.blinkblinkbeach.data.repositories.profile

import android.net.Uri
import com.catness.blinkblinkbeach.data.model.User

interface ProfileRepository {
    suspend fun getCurrentUser(): User
    suspend fun uploadImageAndReturnUser(uri: Uri): User
    suspend fun updateUsername(username: String): User
}
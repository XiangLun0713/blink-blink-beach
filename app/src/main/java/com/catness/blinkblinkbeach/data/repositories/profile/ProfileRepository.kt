package com.catness.blinkblinkbeach.data.repositories.profile

import com.catness.blinkblinkbeach.data.model.User

interface ProfileRepository {
    suspend fun getCurrentUser(): User
}
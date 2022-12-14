package com.catness.blinkblinkbeach.data.repositories.main

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : MainRepository {
    override fun checkIfUserIsAuthenticated() = firebaseAuth.currentUser != null
}
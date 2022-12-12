package com.catness.blinkblinkbeach.ui.signIn

import androidx.lifecycle.ViewModel
import com.catness.blinkblinkbeach.data.repositories.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}
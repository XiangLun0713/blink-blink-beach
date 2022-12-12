package com.catness.blinkblinkbeach.ui.signIn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.repositories.auth.AuthRepository
import com.catness.blinkblinkbeach.data.repositories.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val email = state.getLiveData("sign_in_email", "")
    val password = state.getLiveData("sign_in_password", "")

    private val signInEventChannel = Channel<SignInEvent>()
    val signInEvent = signInEventChannel.receiveAsFlow()

    private val authStateChannel = Channel<AuthState>()
    val authState = authStateChannel.receiveAsFlow()

    fun onSignInButtonClick() = viewModelScope.launch {
        authStateChannel.send(AuthState.Loading)
        authStateChannel.send(repository.signIn(email.value.toString(), password.value.toString()))
    }

    fun onNavigateToSignUpButtonClick() = viewModelScope.launch {
        signInEventChannel.send(SignInEvent.NavigateToSignUpFragment)
    }

    sealed class SignInEvent {
        object NavigateToSignUpFragment : SignInEvent()
    }
}
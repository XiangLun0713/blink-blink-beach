package com.catness.blinkblinkbeach.ui.signUp

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
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val username = state.getLiveData("sign_up_username", "")
    val email = state.getLiveData("sign_up_email", "")
    val password = state.getLiveData("sign_up_password", "")

    private val signUpEventChannel = Channel<SignUpEvent>()
    val signUpEvent = signUpEventChannel.receiveAsFlow()

    private val authStateChannel = Channel<AuthState>()
    val authState = authStateChannel.receiveAsFlow()

    fun onNavigateToSignInButtonClick() = viewModelScope.launch {
        signUpEventChannel.send(SignUpEvent.NavigateToSignInFragment)
    }

    fun onSignUpButtonClick() = viewModelScope.launch {
        authStateChannel.send(AuthState.Loading)
        authStateChannel.send(
            repository.signUp(
                username.value.toString(),
                email.value.toString(),
                password.value.toString()
            )
        )
    }

    sealed class SignUpEvent {
        object NavigateToSignInFragment : SignUpEvent()
    }
}
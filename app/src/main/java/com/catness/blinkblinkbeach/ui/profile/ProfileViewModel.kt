package com.catness.blinkblinkbeach.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.User
import com.catness.blinkblinkbeach.data.repositories.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch {
            _user.value = repository.getCurrentUser()
        }
    }

    fun uploadUserProfileImage(uri: Uri) = viewModelScope.launch {
        val userData = repository.uploadImageAndReturnUser(uri)
        _user.value = userData
    }

    fun updateUsername(username: String) = viewModelScope.launch {
        val userData = repository.updateUsername(username)
        _user.value = userData
    }
}
package com.catness.blinkblinkbeach.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.data.repositories.notification.NotificationRepository
import com.catness.blinkblinkbeach.data.repositories.notification.NotificationsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
) : ViewModel() {

    private val _notificationsResponse = MutableLiveData<NotificationsResponse>(Response.Loading)
    val notificationsResponse: LiveData<NotificationsResponse> = _notificationsResponse

    init {
        getNotifications()
    }

    private fun getNotifications() = viewModelScope.launch {
        repository.getNotificationsFromFirestore().collect { response ->
            _notificationsResponse.value = response
        }
    }
}
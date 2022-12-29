package com.catness.blinkblinkbeach.ui.eventDetail

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.data.repositories.event.EventRepository
import com.catness.blinkblinkbeach.utilities.APIState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val apiStateChannel = Channel<APIState>()
    val apiState = apiStateChannel.receiveAsFlow()

    private val _uid = MutableLiveData("")
    val uid: LiveData<String> = _uid

    init {
        _uid.value = firebaseAuth.uid
    }

    fun loadEventImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }

    fun onRegisterEventButtonClick(eventId: String) = viewModelScope.launch {
        apiStateChannel.send(APIState.Loading)
        apiStateChannel.send(repository.registerEvent(eventId))
    }
}
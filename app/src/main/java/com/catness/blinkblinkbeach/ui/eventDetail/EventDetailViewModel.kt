package com.catness.blinkblinkbeach.ui.eventDetail

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.data.repositories.event.EventRepository
import com.catness.blinkblinkbeach.utilities.APIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val apiStateChannel = Channel<APIState>()
    val apiState = apiStateChannel.receiveAsFlow()

    fun loadEventImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }

    fun onRegisterEventButtonClick(eventId: String) = viewModelScope.launch {
        // todo fix unable to register error
        apiStateChannel.send(APIState.Loading)
        apiStateChannel.send(repository.registerEvent(eventId))
    }
}
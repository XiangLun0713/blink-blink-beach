package com.catness.blinkblinkbeach.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.data.repositories.home.EventsResponse
import com.catness.blinkblinkbeach.data.repositories.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : ViewModel() {

    private val _eventsResponse = MutableLiveData<EventsResponse>(Response.Loading)
    val eventsResponse: LiveData<EventsResponse> = _eventsResponse

    init {
        getEvents()
    }

    private fun getEvents() = viewModelScope.launch {
        repository.getEventsFromFirestore().collect { response ->
            _eventsResponse.value = response
        }
    }
}
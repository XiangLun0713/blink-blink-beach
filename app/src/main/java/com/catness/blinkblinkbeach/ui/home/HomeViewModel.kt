package com.catness.blinkblinkbeach.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.data.repositories.home.HomeRepository
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _eventList = MutableLiveData<APIStateWithValue<List<Event>>>()
    val eventList: LiveData<APIStateWithValue<List<Event>>> = _eventList

    fun populateEventList() = viewModelScope.launch {
        _eventList.value = repository.fetchEventList()
    }
}
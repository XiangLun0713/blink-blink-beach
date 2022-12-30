package com.catness.blinkblinkbeach.ui.home

import androidx.lifecycle.*
import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.data.repositories.eventList.PreferencesManager
import com.catness.blinkblinkbeach.data.repositories.eventList.SortOrder
import com.catness.blinkblinkbeach.data.repositories.home.EventsResponse
import com.catness.blinkblinkbeach.data.repositories.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _eventsResponse = MutableLiveData<EventsResponse>(Response.Loading)
    val eventsResponse: LiveData<EventsResponse> = _eventsResponse

    private val _eventList = MutableStateFlow<List<Event>>(listOf())

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    init {
        getEvents()
    }

    private fun getEvents() = viewModelScope.launch {
        repository.getEventsFromFirestore().collect { response ->
            _eventsResponse.value = response
            if (response is Response.Success) {
                _eventList.value = response.data
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val eventsFlow: Flow<List<Event>> =
        combine(searchQuery, preferencesFlow, _eventList) { query, sortOrder, eventList ->
            Triple(query, sortOrder, eventList)
        }.flatMapLatest { (query, sortOrder, eventList) ->
            filterEvents(query,
                sortOrder,
                eventList)
        }

    val events = eventsFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    private fun filterEvents(
        query: String,
        sortOrder: SortOrder,
        list: List<Event>,
    ): Flow<List<Event>> = flow {
        val modifiedList = mutableListOf<Event>()
        for (event in list) {
            if (event.name.contains(query, ignoreCase = true)) {
                modifiedList.add(event)
            }
        }
        when (sortOrder) {
            SortOrder.BY_NAME -> {
                modifiedList.sortWith(compareBy { it.name })
            }
            SortOrder.BY_DATE -> {
                modifiedList.sortWith(compareBy { it.startTimeMillis })
            }
        }
        emit(modifiedList)
    }
}

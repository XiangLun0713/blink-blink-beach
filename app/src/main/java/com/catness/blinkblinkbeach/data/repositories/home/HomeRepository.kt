package com.catness.blinkblinkbeach.data.repositories.home

import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.data.model.Response
import kotlinx.coroutines.flow.Flow

typealias Events = List<Event>
typealias EventsResponse = Response<Events>

interface HomeRepository {
    fun getEventsFromFirestore(): Flow<EventsResponse>
    fun getPastEventsFromFirestore(): Flow<EventsResponse>
}
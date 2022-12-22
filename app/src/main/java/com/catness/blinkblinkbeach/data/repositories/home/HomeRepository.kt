package com.catness.blinkblinkbeach.data.repositories.home

import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.utilities.APIStateWithValue

interface HomeRepository {
    suspend fun fetchEventList(): APIStateWithValue<List<Event>>
}
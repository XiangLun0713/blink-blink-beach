package com.catness.blinkblinkbeach.data.repositories.event

import com.catness.blinkblinkbeach.utilities.APIState

interface EventRepository {
    suspend fun registerEvent(eventId: String): APIState
}
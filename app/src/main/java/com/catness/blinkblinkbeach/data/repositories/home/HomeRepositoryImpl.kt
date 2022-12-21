package com.catness.blinkblinkbeach.data.repositories.home

import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : HomeRepository {

    override suspend fun fetchEventList(): APIStateWithValue<List<Event>> {
        val eventsCollection = firestore.collection("events")
        val eventList = mutableListOf<Event>()

        return try {
            val eventListSnapshot: QuerySnapshot =
                eventsCollection.whereGreaterThan("startTimeMillis", System.currentTimeMillis())
                    .get().await()
            for (eventSnapshot in eventListSnapshot) {
                val event = eventSnapshot.toObject(Event::class.java)
                eventList.add(event)
            }
            APIStateWithValue.Success(eventList.sortedBy { it.startTimeMillis })
        } catch (e: Exception) {
            APIStateWithValue.Error("Fail to fetch Event: ${e.message}")
        }
    }
}
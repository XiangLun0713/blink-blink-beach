package com.catness.blinkblinkbeach.data.repositories.home

import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : HomeRepository {

    private val eventsCollection = firestore.collection(Constants.EVENTS)

    override fun getEventsFromFirestore() = callbackFlow {
        val snapshotListener =
            eventsCollection.whereGreaterThan("startTimeMillis", System.currentTimeMillis())
                .addSnapshotListener { snapshot, e ->
                    val eventsResponse = if (snapshot != null) {
                        val events = snapshot.toObjects(Event::class.java)
                        Response.Success(events)
                    } else {
                        Response.Failure(e)
                    }
                    trySend(eventsResponse)
                }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getPastEventsFromFirestore() = callbackFlow {
        val snapshotListener =
            eventsCollection.whereLessThan("endTimeMillis", System.currentTimeMillis())
                .addSnapshotListener { snapshot, e ->
                    val eventsResponse = if (snapshot != null) {
                        val events = snapshot.toObjects(Event::class.java)
                        Response.Success(events)
                    } else {
                        Response.Failure(e)
                    }
                    trySend(eventsResponse)
                }

        awaitClose {
            snapshotListener.remove()
        }
    }
}
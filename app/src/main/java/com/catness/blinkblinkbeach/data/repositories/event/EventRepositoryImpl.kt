package com.catness.blinkblinkbeach.data.repositories.event

import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.data.model.User
import com.catness.blinkblinkbeach.utilities.APIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : EventRepository {

    override suspend fun registerEvent(eventId: String): APIState {
        val usersCollection = firestore.collection("users")
        val eventsCollection = firestore.collection("events")

        return try {
            val uid = firebaseAuth.uid ?: throw Exception()

            // add the event's id to the user's event participation list
            val userSnapshot = usersCollection.document(uid).get().await()
            val user = userSnapshot.toObject(User::class.java) ?: throw Exception()
            val newUser = user.copy(eventsParticipated = user.eventsParticipated + eventId)
            usersCollection.document(uid).set(newUser).await()

            // add the user's id to the event's participant list
            val eventSnapshot = eventsCollection.document(eventId).get().await()
            val event = eventSnapshot.toObject(Event::class.java) ?: throw Exception()
            if (event.participantIDs.contains(uid)) throw Exception("You already registered in this event!")
            val newEvent = event.copy(participantIDs = event.participantIDs + uid)
            eventsCollection.document(event.eventID).set(newEvent).await()

            APIState.Success
        } catch (e: Exception) {
            APIState.Error("Register event failed: ${e.message}")
        }
    }
}
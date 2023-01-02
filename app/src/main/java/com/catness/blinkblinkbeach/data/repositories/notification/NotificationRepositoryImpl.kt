package com.catness.blinkblinkbeach.data.repositories.notification

import com.catness.blinkblinkbeach.data.model.Notification
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : NotificationRepository {

    private val notificationsCollection = firestore.collection(Constants.NOTIFICATIONS)

    override fun getNotificationsFromFirestore() = callbackFlow {
        val snapshotListener =
            notificationsCollection.addSnapshotListener { snapshot, e ->
                val notificationsResponse = if (snapshot != null) {
                    val notifications = snapshot.toObjects(Notification::class.java)
                    notifications.sortByDescending { it.dateInMillis }
                    Response.Success(notifications)
                } else {
                    Response.Failure(e)
                }
                trySend(notificationsResponse)
            }

        awaitClose {
            snapshotListener.remove()
        }
    }
}
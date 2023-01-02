package com.catness.blinkblinkbeach.data.repositories.notification

import com.catness.blinkblinkbeach.data.model.Notification
import com.catness.blinkblinkbeach.data.model.Response
import kotlinx.coroutines.flow.Flow

typealias Notifications = List<Notification>
typealias NotificationsResponse = Response<Notifications>

interface NotificationRepository {
    fun getNotificationsFromFirestore(): Flow<NotificationsResponse>
}
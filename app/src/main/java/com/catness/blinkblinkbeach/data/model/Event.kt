package com.catness.blinkblinkbeach.data.model

import android.os.Parcelable
import com.catness.blinkblinkbeach.utilities.EventStatus
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class Event(
    val eventID: String,
    val longitude: Double,
    val latitude: Double,
    val address: String,
    val name: String,
    val date: String = DateFormat.getDateTimeInstance().format(System.currentTimeMillis()),
    val imageUrl: String,
    val status: EventStatus,
    val detail: String,
    val participantIDs: List<String>,
) : Parcelable

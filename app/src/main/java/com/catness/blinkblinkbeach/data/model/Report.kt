package com.catness.blinkblinkbeach.data.model

import android.os.Parcelable
import com.catness.blinkblinkbeach.utilities.ReportStatus
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class Report(
    val id: String,
    val longitude: Double,
    val latitude: Double,
    val date: String = DateFormat.getDateTimeInstance().format(System.currentTimeMillis()),
    val pictureUrl: String,
    val status: ReportStatus,
    val reporterID: String,
) : Parcelable
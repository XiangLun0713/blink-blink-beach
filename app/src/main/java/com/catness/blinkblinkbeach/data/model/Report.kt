package com.catness.blinkblinkbeach.data.model

import android.os.Parcelable
import com.catness.blinkblinkbeach.utilities.ReportStatus
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class Report(
    var id: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var date: String = DateFormat.getDateTimeInstance().format(System.currentTimeMillis()),
    var imageUrl: String = "",
    var status: ReportStatus = ReportStatus.PENDING,
    var reporterID: String = "",
) : Parcelable
package com.catness.blinkblinkbeach.data.repositories.report

import android.net.Uri
import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.catness.blinkblinkbeach.utilities.ReportStatus

interface ReportRepository {
    suspend fun fetchReportList(status: ReportStatus): APIStateWithValue<List<Report>>
    suspend fun submitReport(
        description: String,
        latitude: Double,
        longitude: Double,
        fileUri: Uri
    ): APIStateWithValue<Report>
}
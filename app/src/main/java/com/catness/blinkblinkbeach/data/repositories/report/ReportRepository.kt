package com.catness.blinkblinkbeach.data.repositories.report

import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.catness.blinkblinkbeach.utilities.ReportStatus

interface ReportRepository {
    suspend fun fetchReportList(status: ReportStatus): APIStateWithValue<List<Report>>
}
package com.catness.blinkblinkbeach.data.repositories.report

import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.utilities.APIStateWithValue

interface ReportRepository {
    suspend fun fetchReportList(): APIStateWithValue<List<Report>>
}
package com.catness.blinkblinkbeach.data.repositories.report

import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestoreAuth: FirebaseAuth
) : ReportRepository {

    override suspend fun fetchReportList(): APIStateWithValue<List<Report>> {
        val userID = firestoreAuth.uid

        val reportsCollection = firestore.collection("reports").whereEqualTo("reporterID", userID)
        val reportList = mutableListOf<Report>()

        return try {
            val reportListSnapshot: QuerySnapshot =
                reportsCollection.get().await()
            for (reportSnapshot in reportListSnapshot) {
                val report = reportSnapshot.toObject(Report::class.java)
                report.id = reportSnapshot.id
                reportList.add(report)
            }
            APIStateWithValue.Success(reportList.sortedBy { it.date })
        } catch (e: Exception) {
            APIStateWithValue.Error("${e.message}")
        }
    }
}
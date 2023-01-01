package com.catness.blinkblinkbeach.data.repositories.report

import android.net.Uri
import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.catness.blinkblinkbeach.utilities.ReportStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val firebaseStorage: FirebaseStorage,
    private val firestoreAuth: FirebaseAuth
) : ReportRepository {

    val reportsCollection = firestore.collection("reports")

    override suspend fun fetchReportList(status: ReportStatus): APIStateWithValue<List<Report>> {
        val userID = firestoreAuth.uid

        val snapshot = reportsCollection.whereEqualTo("reporterID", userID)
            .whereEqualTo("status", status)
        val reportList = mutableListOf<Report>()

        return try {
            val reportListSnapshot: QuerySnapshot =
                snapshot.get().await()
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

    override suspend fun submitReport(
        description: String,
        latitude: Double,
        longitude: Double,
        fileUri: Uri
    ): APIStateWithValue<Report> {
        try {
            val childRef = storageReference.child("report/${fileUri.lastPathSegment}")
            val uploadTask = childRef.putFile(fileUri).await()
            val imageUrl = uploadTask.metadata?.reference?.downloadUrl?.await() ?: throw Exception()

            val newReport = Report()
            newReport.description = description
            newReport.latitude = latitude
            newReport.longitude = longitude
            newReport.imageUrl = imageUrl.toString()
            newReport.reporterID = firestoreAuth.uid.toString()
            newReport.status = ReportStatus.PENDING

            reportsCollection.add(newReport)

            return APIStateWithValue.Success(newReport)
        } catch (e: Exception) {
            return APIStateWithValue.Error("${e.message}")
        }
    }
}
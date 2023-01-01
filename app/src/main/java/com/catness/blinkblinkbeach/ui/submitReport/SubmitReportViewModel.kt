package com.catness.blinkblinkbeach.ui.submitReport

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.data.repositories.report.ReportRepository
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubmitReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {
    private val _report = MutableLiveData<APIStateWithValue<Report>>()
    val report: LiveData<APIStateWithValue<Report>> = _report

    fun submitReport(
        description: String,
        latitude: Double,
        longitude: Double,
        fileUri: Uri
    ) = viewModelScope.launch {
        val report = repository.submitReport(description, latitude, longitude, fileUri)
        _report.value = report
    }
}
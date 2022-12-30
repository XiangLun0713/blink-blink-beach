package com.catness.blinkblinkbeach.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.data.repositories.report.ReportRepository
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.catness.blinkblinkbeach.utilities.ReportStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _reportList = MutableLiveData<APIStateWithValue<List<Report>>>()
    val reportList: LiveData<APIStateWithValue<List<Report>>> = _reportList

    fun populateReportList(status: ReportStatus) = viewModelScope.launch {
        _reportList.value = repository.fetchReportList(status)
    }

    fun clearCurrentReportList() = viewModelScope.launch {
        _reportList.value = APIStateWithValue.Success(emptyList())
    }
}
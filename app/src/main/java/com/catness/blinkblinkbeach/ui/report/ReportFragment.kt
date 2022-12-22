package com.catness.blinkblinkbeach.ui.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment(R.layout.fragment_report) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReportBinding.bind(view)
        binding.apply {
        }
    }
}
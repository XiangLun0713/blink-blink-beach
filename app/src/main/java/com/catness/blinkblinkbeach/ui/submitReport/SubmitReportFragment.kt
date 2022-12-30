package com.catness.blinkblinkbeach.ui.submitReport

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentSubmitReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubmitReportFragment : Fragment(R.layout.fragment_submit_report) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSubmitReportBinding.bind(view)

    }
}
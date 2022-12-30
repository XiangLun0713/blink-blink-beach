package com.catness.blinkblinkbeach.ui.report

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentReportBinding
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import com.catness.blinkblinkbeach.utilities.ReportStatus
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment(R.layout.fragment_report) {

    private val viewModel: ReportViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReportBinding.bind(view)

        viewModel.populateReportList(ReportStatus.PENDING)

        binding.apply {
            reportTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            viewModel.clearCurrentReportList()
                            viewModel.populateReportList(ReportStatus.PENDING)
                            return
                        }
                        1 -> {
                            viewModel.clearCurrentReportList()
                            viewModel.populateReportList(ReportStatus.APPROVED)
                            return
                        }
                        else -> {}
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })

            viewModel.reportList.observe(viewLifecycleOwner) {
                when (it) {
                    is APIStateWithValue.Error -> {
                        Toast.makeText(
                            context,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is APIStateWithValue.Success -> {
                        reportRecyclerView.adapter =
                            ReportAdapter(
                                it.result,
                                requireContext(),
                                findNavController()
                            )
                    }
                    else -> {}
                }
            }
        }
    }
}
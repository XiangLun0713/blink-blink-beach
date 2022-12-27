package com.catness.blinkblinkbeach.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.catness.blinkblinkbeach.data.model.Report
import com.catness.blinkblinkbeach.databinding.ReportCardTemplateBinding

class ReportAdapter(
    private val reportList: List<Report>,
    private val context: Context,
    private val navController: NavController
) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    class ViewHolder(val binding: ReportCardTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ReportCardTemplateBinding.inflate(layoutInflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reportList[position]
        holder.binding.apply {
            reportIdText.text = report.id
            reportDateTimeTextView.text = report.date
        }
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

}
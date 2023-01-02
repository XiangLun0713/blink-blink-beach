package com.catness.blinkblinkbeach.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catness.blinkblinkbeach.data.model.Notification
import com.catness.blinkblinkbeach.databinding.NotificationCardTemplateBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val notificationList: List<Notification>,
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: NotificationCardTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NotificationCardTemplateBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.binding.apply {
            notificationDescriptionTextView.text = notification.description
            notificationDateTextView.text = getDateFromMillis(notification.dateInMillis)
        }
    }

    private fun getDateFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(calendar.time)
    }

    override fun getItemCount() = notificationList.size
}
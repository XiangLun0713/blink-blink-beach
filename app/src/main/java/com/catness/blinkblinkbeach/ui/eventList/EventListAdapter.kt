package com.catness.blinkblinkbeach.ui.eventList

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.NavGraphDirections
import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.databinding.EventBannerTemplateBinding
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(
    private val eventList: List<Event>,
    private val context: Context,
    private val navController: NavController,
) :
    RecyclerView.Adapter<EventListAdapter.EventListViewHolder>() {

    class EventListViewHolder(val binding: EventBannerTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventBannerTemplateBinding.inflate(layoutInflater, parent, false)
        return EventListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val event = eventList[position]
        holder.binding.apply {
            Glide.with(context)
                .load(Uri.parse(event.imageUrl))
                .into(eventBannerImageView)

            eventBannerName.text = event.name
            eventBannerDate.text = getDateFromMillis(event.startTimeMillis)

            root.setOnClickListener {
                val action = NavGraphDirections.actionGlobalEventDetailFragment(event)
                navController.navigate(action)
            }
        }
    }

    private fun getDateFromMillis(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(calendar.time)
    }

    override fun getItemCount() = eventList.size
}
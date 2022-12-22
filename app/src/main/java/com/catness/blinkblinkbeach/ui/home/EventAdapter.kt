package com.catness.blinkblinkbeach.ui.home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.NavGraphDirections
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.databinding.EventCardTemplateBinding
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(
    private val eventList: List<Event>,
    private val context: Context,
    private val navController: NavController
) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(val binding: EventCardTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventCardTemplateBinding.inflate(layoutInflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.binding.apply {
            Glide.with(context)
                .load(Uri.parse(event.imageUrl))
                .into(eventCardHeaderImageView)
            eventCardNumOfParticipantTextView.text =
                context.resources
                    .getString(R.string.participant_joined, event.participantIDs.size)
            eventCardTitle.text = event.name
            eventCardDateTextView.text = context.resources.getString(
                R.string.date_with_comma,
                getDateFromMillis(event.startTimeMillis)
            )
            eventCardLocationTextView.text =
                context.resources.getString(R.string.location_with_comma, event.address)
            eventCardDetailsButton.setOnClickListener {
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
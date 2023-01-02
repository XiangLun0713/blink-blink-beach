package com.catness.blinkblinkbeach.ui.notification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotificationBinding.bind(view)

        binding.apply {
            viewModel.notificationsResponse.observe(viewLifecycleOwner) { notificationsResponse ->
                when (notificationsResponse) {
                    is Response.Success -> {
                        val notificationList = notificationsResponse.data

                        // save recycler view state
                        val recyclerViewState =
                            notificationRecyclerView.layoutManager?.onSaveInstanceState()

                        // update recycler view
                        notificationRecyclerView.adapter =
                            NotificationAdapter(
                                notificationList
                            )

                        // restore recycler view state
                        notificationRecyclerView.layoutManager?.onRestoreInstanceState(
                            recyclerViewState)

                    }
                    is Response.Failure -> {
                        Toast.makeText(
                            context,
                            notificationsResponse.e?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }
}
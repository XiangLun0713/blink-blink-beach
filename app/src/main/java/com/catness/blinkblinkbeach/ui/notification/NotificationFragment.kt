package com.catness.blinkblinkbeach.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNotificationBinding.bind(view)
    }
}
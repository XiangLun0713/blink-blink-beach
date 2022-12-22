package com.catness.blinkblinkbeach.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
    }
}
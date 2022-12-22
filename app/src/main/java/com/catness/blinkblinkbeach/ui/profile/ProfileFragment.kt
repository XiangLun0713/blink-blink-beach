package com.catness.blinkblinkbeach.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)

        binding.apply {

            viewModel.user.observe(viewLifecycleOwner) { user ->
                if (!user.profileImageUrl.isEmpty()) {
                    Glide.with(profileImage.context)
                        .load(user.profileImageUrl)
                        .into(profileImage)
                }

                numberOfJoinedEventTextView.text =
                    requireContext().resources.getString(
                        R.string.number,
                        user.eventsParticipated.size
                    )

                usernameTextView.text = user.username

                emailTextView.text = user.email
            }

            editProfileImageButton.setOnClickListener {
                // todo navigate to edit profile fragment
            }

            editUsernameButton.setOnClickListener {
                // todo show dialog box
            }

            submitReportButton.setOnClickListener {
                // todo navigate to my report page
            }

            trackMyReportButton.setOnClickListener {
                // todo submit report page
            }
        }
    }
}
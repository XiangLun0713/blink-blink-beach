package com.catness.blinkblinkbeach.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.User
import com.catness.blinkblinkbeach.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var user: User
    private val args: ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)

        user = args.user

        binding.apply {
            Glide.with(profileImage.context)
                .load(user.profileImageUrl)
                .into(profileImage)

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

            submitReportButton

            numberOfJoinedEventTextView.text =
                requireContext().resources.getString(R.string.number, user.eventsParticipated.size)

            usernameTextView.text = user.username

            emailTextView.text = user.email

        }
    }
}
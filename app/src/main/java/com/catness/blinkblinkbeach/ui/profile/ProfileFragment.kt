package com.catness.blinkblinkbeach.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.catness.blinkblinkbeach.NavGraphDirections
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.DialogChangeUsernameBinding
import com.catness.blinkblinkbeach.databinding.FragmentProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var imageView: CircleImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)

        binding.apply {

            imageView = profileImage

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
                ImagePicker.with(requireActivity())
                    .crop()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            editUsernameButton.setOnClickListener {
                // inflate alert dialog box
                val customDialogBinding = DialogChangeUsernameBinding.inflate(layoutInflater)
                // build and show alert dialog box
                val builder = AlertDialog.Builder(activity)
                    .setView(customDialogBinding.root)
                val alertDialog = builder.show()
                // view binding
                customDialogBinding.apply {
                    dialogCancelButton.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    dialogSendEmailButton.setOnClickListener {
                        alertDialog.dismiss()
                        viewModel.updateUsername(
                            dialogUsernameEditText.text.toString()
                        )
                    }
                }
            }

            submitReportButton.setOnClickListener {
                // todo navigate to my report page
            }

            trackMyReportButton.setOnClickListener {
                // navigate to report page
                val action = NavGraphDirections.actionGlobalReportFragment()
                findNavController().navigate(action)
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    // upload profile image
                    viewModel.uploadUserProfileImage(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}
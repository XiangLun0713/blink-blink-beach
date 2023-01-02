package com.catness.blinkblinkbeach.ui.submitReport

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentSubmitReportBinding
import com.catness.blinkblinkbeach.ui.maps.MapsActivityCurrentPlace
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_submit_report.*


@AndroidEntryPoint
class SubmitReportFragment : Fragment(R.layout.fragment_submit_report) {
    private lateinit var binding: FragmentSubmitReportBinding
    private lateinit var fileUri: Uri
    private val viewModel: SubmitReportViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSubmitReportBinding.bind(view)

        binding.apply {
            uploadImageBtn.setOnClickListener {
                ImagePicker.with(requireActivity()).crop().galleryOnly()
                    .createIntent { intent -> startForUploadImageResult.launch(intent) }
            }

            cameraBtn.setOnClickListener {
                ImagePicker.with(requireActivity()).crop().cameraOnly()
                    .createIntent { intent -> startForUploadImageResult.launch(intent) }
            }

            locationBtn.setOnClickListener {
                val intent = Intent(requireContext(), MapsActivityCurrentPlace::class.java)
                startLocationPicker.launch(intent)
            }

            submitBtn.setOnClickListener {
                val description = reportDescriptionEditText.text.toString()
                val splitLocation = locationEditText.text?.split(",")
                if (splitLocation?.size!! > 1) {
                    val latitude = splitLocation[0].toDoubleOrNull()
                    val longitude = splitLocation[1].toDoubleOrNull()
                    if (latitude != null && longitude != null) {
                        submitBtn.isEnabled = false
                        viewModel.submitReport(description, latitude, longitude, fileUri)
                            .invokeOnCompletion {
                                findNavController().navigateUp()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Invalid Location", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private val startForUploadImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    fileUri = data?.data!!
                    binding.evidenceEditText.text = SpannableStringBuilder(fileUri.lastPathSegment)
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

    private val startLocationPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val latitude = data?.getDoubleExtra("latitude", 0.0)
                    val longitude = data?.getDoubleExtra("longitude", 0.0)

                    binding.locationEditText.text = SpannableStringBuilder("$latitude, $longitude")
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}
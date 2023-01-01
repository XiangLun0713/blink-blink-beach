package com.catness.blinkblinkbeach.ui.submitReport

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentSubmitReportBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_submit_report.*

@AndroidEntryPoint
class SubmitReportFragment : Fragment(R.layout.fragment_submit_report) {
    private var binding: FragmentSubmitReportBinding? = null

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
        }
    }

    private val startForUploadImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri: Uri = data?.data!!
                    Log.d("FUCK", fileUri.toString())
                    val fileName = fileUri.toString().split("/").last()
                    Log.d("FUCK", fileName)
                    binding?.evidenceTextInput?.hint = fileName
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
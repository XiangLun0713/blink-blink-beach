package com.catness.blinkblinkbeach.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.FragmentHomeBinding
import com.catness.blinkblinkbeach.utilities.APIStateWithValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)
        viewModel.populateEventList()

        binding.apply {
            viewAllButton.setOnClickListener {
                // todo navigate to event list page
            }

            viewModel.eventList.observe(viewLifecycleOwner) {
                when (it) {
                    is APIStateWithValue.Error -> {
                        Toast.makeText(
                            context,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is APIStateWithValue.Success -> {
                        eventRecyclerView.adapter =
                            EventAdapter(
                                it.result.subList(0, 5),
                                requireContext(),
                                findNavController()
                            )
                    }
                    else -> {}
                }
            }
        }
    }
}
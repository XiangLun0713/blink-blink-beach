package com.catness.blinkblinkbeach.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)

        binding.apply {
            viewAllButton.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToEventListFragment()
                findNavController().navigate(action)
            }

            viewModel.eventsResponse.observe(viewLifecycleOwner) { eventsResponse ->
                when (eventsResponse) {
                    is Response.Success -> {
                        // save recycler view state
                        val recyclerViewState =
                            eventRecyclerView.layoutManager?.onSaveInstanceState()

                        // update recycler view
                        eventRecyclerView.adapter =
                            EventAdapter(
                                eventsResponse.data.subList(0, 5),
                                requireContext(),
                                findNavController()
                            )

                        // restore recycler view state
                        eventRecyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    }
                    is Response.Failure -> {
                        Toast.makeText(
                            context,
                            eventsResponse.e?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }

}

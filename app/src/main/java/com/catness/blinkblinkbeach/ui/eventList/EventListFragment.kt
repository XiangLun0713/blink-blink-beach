package com.catness.blinkblinkbeach.ui.eventList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.Response
import com.catness.blinkblinkbeach.databinding.FragmentEventListBinding
import com.catness.blinkblinkbeach.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEventListBinding.bind(view)

        binding.apply {

            viewModel.eventsResponse.observe(viewLifecycleOwner) { eventsResponse ->
                when (eventsResponse) {
                    is Response.Success -> {
                        // save recycler view state
                        val recyclerViewState =
                            eventBannerRecyclerView.layoutManager?.onSaveInstanceState()

                        // update recycler view
                        eventBannerRecyclerView.adapter =
                            EventListAdapter(
                                eventsResponse.data,
                                requireContext(),
                                findNavController()
                            )

                        // restore recycler view state
                        eventBannerRecyclerView.layoutManager?.onRestoreInstanceState(
                            recyclerViewState)
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
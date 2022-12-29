package com.catness.blinkblinkbeach.ui.eventList

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.repositories.eventList.SortOrder
import com.catness.blinkblinkbeach.databinding.FragmentEventListBinding
import com.catness.blinkblinkbeach.ui.home.HomeViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private val viewModel: HomeViewModel by viewModels()

    // todo change overflow menu in this fragment, to include the search action
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.event_list_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    companion object {
        const val DATE = "Date"
        const val NAME = "Name"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEventListBinding.bind(view)

        binding.apply {

            val options = arrayOf(DATE, NAME)
            val autoCompleteTextView = (sortByTextView as? MaterialAutoCompleteTextView)
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.preferencesFlow.collect { sortOrder ->
                    autoCompleteTextView?.setText(
                        if (sortOrder == SortOrder.BY_NAME) {
                            "Name"
                        } else {
                            "Date"
                        }, false)
                }
            }
            autoCompleteTextView?.setSimpleItems(options)
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
            autoCompleteTextView?.setAdapter(adapter)
            autoCompleteTextView?.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    // Handle the selection
                    when (options[position]) {
                        DATE -> {
                            viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                        }
                        NAME -> {
                            viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                        }
                    }
                }

            viewModel.events.observe(viewLifecycleOwner) { eventList ->
                // save recycler view state
                val recyclerViewState =
                    eventBannerRecyclerView.layoutManager?.onSaveInstanceState()

                // update recycler view
                eventBannerRecyclerView.adapter =
                    EventListAdapter(
                        eventList,
                        requireContext(),
                        findNavController()
                    )

                // restore recycler view state
                eventBannerRecyclerView.layoutManager?.onRestoreInstanceState(
                    recyclerViewState)

            }

        }
    }
}
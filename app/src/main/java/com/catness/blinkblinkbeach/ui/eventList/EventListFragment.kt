package com.catness.blinkblinkbeach.ui.eventList

import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.repositories.eventList.SortOrder
import com.catness.blinkblinkbeach.databinding.FragmentEventListBinding
import com.catness.blinkblinkbeach.ui.home.HomeViewModel
import com.catness.blinkblinkbeach.utilities.onQueryTextChanged
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment(R.layout.fragment_event_list) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentEventListBinding

    companion object {
        const val DATE = "Date"
        const val NAME = "Name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
        requireActivity().menuInflater.inflate(R.menu.event_list_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        if (!viewModel.searchQuery.value.isEmpty()) searchView.setIconified(false)
        searchView.queryHint = Html.fromHtml("<font color = #fafafa>Search by name...</font>")

        val searchViewEditText =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        searchViewEditText.setTextColor(getResources().getColor(R.color.md_theme_light_onPrimary))
        searchViewEditText.setText(viewModel.searchQuery.value)
        searchViewEditText.setSelection(searchViewEditText.length())
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventListBinding.bind(view)

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

            // set up auto complete text view
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

            // observe events and populate event list
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

    override fun onResume() {
        super.onResume()
        // set up the auto complete text view
        val options = arrayOf(DATE, NAME)
        val autoCompleteTextView = (binding.sortByTextView as? MaterialAutoCompleteTextView)
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

        requireActivity().invalidateOptionsMenu()
    }
}
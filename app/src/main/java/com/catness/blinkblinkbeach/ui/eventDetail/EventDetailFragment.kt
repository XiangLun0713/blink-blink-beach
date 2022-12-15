package com.catness.blinkblinkbeach.ui.eventDetail

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.model.Event
import com.catness.blinkblinkbeach.databinding.FragmentEventDetailBinding
import com.catness.blinkblinkbeach.utilities.APIState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment(R.layout.fragment_event_detail), OnMapReadyCallback {

    private lateinit var eventMapView: MapView
    private lateinit var scrollView: ScrollView
    private lateinit var event: Event
    private val args: EventDetailFragmentArgs by navArgs()
    private val viewModel: EventDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEventDetailBinding.bind(view)

        scrollView = binding.eventScrollView
        eventMapView = binding.eventMapView
        event = args.event

        // set up google map view
        binding.eventMapView.onCreate(savedInstanceState)
        binding.eventMapView.getMapAsync(this)

        binding.apply {
            viewModel.loadEventImage(eventImageView, event.imageUrl)
            eventHeaderTitleTextView.text = event.name
            eventDateTextView.text = event.date
            eventTitleTextView.text = event.name
            eventAddressTextView.text = event.address
            eventDetailTextView.text = event.detail
            eventRegisterButton.setOnClickListener {
                viewModel.onRegisterEventButtonClick(event.eventID)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.apiState.collect { event ->
                    when (event) {
                        is APIState.Error -> {
                            // hide the circular progress indicator
                            eventDetailProgressCardView.visibility = View.INVISIBLE
                            // display error toast message
                            Toast.makeText(
                                this@EventDetailFragment.requireActivity(),
                                event.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is APIState.Loading -> {
                            // show the circular progress indicator
                            eventDetailProgressCardView.visibility = View.VISIBLE
                        }
                        is APIState.Success -> {
                            // hide the circular progress indicator
                            eventDetailProgressCardView.visibility = View.INVISIBLE
                            // display success message
                            Toast.makeText(
                                this@EventDetailFragment.requireActivity(),
                                "Event register successfully.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let { map ->
            // temporary disable the scroll listener of the scroll view when the user is interacting with the map
            map.setOnCameraMoveStartedListener {
                scrollView.requestDisallowInterceptTouchEvent(true)
            }
            map.setOnCameraIdleListener {
                scrollView.requestDisallowInterceptTouchEvent(false)
            }

            val position = LatLng(event.latitude, event.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15f)
            val markerOptions = MarkerOptions()
                .position(position)
                .title("Beach Cleaning Event")
            // add marker on map
            map.addMarker(markerOptions)
            // move the camera to focus on the marker
            map.moveCamera(cameraUpdate)
        }
    }

    override fun onResume() {
        super.onResume()
        eventMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        eventMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        eventMapView.onDestroy()
    }
}
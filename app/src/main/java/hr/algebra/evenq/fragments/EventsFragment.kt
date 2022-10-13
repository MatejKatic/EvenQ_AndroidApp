package hr.algebra.evenq.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.evenq.R
import hr.algebra.evenq.adapters.EventItemRecyclerAdapter
import hr.algebra.evenq.databinding.FragmentEventsBinding
import hr.algebra.evenq.framework.isOnline
import hr.algebra.evenq.framework.showInternetConnectionAlertDialog
import hr.algebra.evenq.viewmodels.EventsViewModel

class EventsFragment: Fragment(R.layout.fragment_events) {

    private lateinit var binding: FragmentEventsBinding

    private val viewModel: EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)

        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())

        viewModel.eventsList.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = ProgressBar.VISIBLE

            val adapter = EventItemRecyclerAdapter(requireContext(), it)
            binding.rvEvents.adapter = adapter

            binding.progressBar.visibility = ProgressBar.INVISIBLE
        })

        binding.swipeContainer.setOnRefreshListener {
            viewModel.getEvents()
            binding.swipeContainer.isRefreshing = false
        }

        if (requireContext().isOnline()){
            viewModel.getEvents()
        } else {
            showInternetConnectionAlertDialog(requireContext())
            binding.progressBar.visibility = ProgressBar.INVISIBLE
        }

        return binding.root
    }

}
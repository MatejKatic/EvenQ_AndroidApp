package hr.algebra.evenq.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.evenq.R
import hr.algebra.evenq.adapters.TicketItemRecyclerAdapter
import hr.algebra.evenq.databinding.FragmentTicketsBinding
import hr.algebra.evenq.framework.isOnline
import hr.algebra.evenq.framework.showInternetConnectionAlertDialog
import hr.algebra.evenq.network.model.Ticket
import hr.algebra.evenq.viewmodels.TicketsViewModel

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private lateinit var binding: FragmentTicketsBinding
    private lateinit var mAuth: FirebaseAuth

    private val viewModel: TicketsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        binding.rvTickets.layoutManager = LinearLayoutManager(requireContext())

        viewModel.ticketsList.observe(viewLifecycleOwner, Observer { ticketList ->
            binding.progressBar.visibility = ProgressBar.VISIBLE

            val validTicketsList = ArrayList(ticketList.filter { it.isValid })

            val adapter = TicketItemRecyclerAdapter(requireContext(), validTicketsList)
            binding.rvTickets.adapter = adapter

            viewModel.insertAllTickets(ticketList)

            binding.progressBar.visibility = ProgressBar.INVISIBLE

            if (adapter.itemCount == 0) {
                binding.tvNoTickets.visibility = View.VISIBLE
            } else {
                binding.tvNoTickets.visibility = View.GONE
            }
        })


        if (requireContext().isOnline()) {
            viewModel.getTicketsForMember(mAuth.currentUser!!.uid)

            binding.swipeContainer.setOnRefreshListener {
                viewModel.getTicketsForMember(mAuth.currentUser!!.uid)
                binding.swipeContainer.isRefreshing = false
            }
        } else {
            viewModel.ticketsFromDb.observe(viewLifecycleOwner) { ticketList ->
                binding.progressBar.visibility = ProgressBar.VISIBLE
                val convertedList = ArrayList<Ticket>()

                val validTicketsList = ArrayList(ticketList.filter { it.isValid })
                validTicketsList.forEach {
                    convertedList.add(it.convertToTicket())
                }

                val adapter = TicketItemRecyclerAdapter(requireContext(), convertedList)
                binding.rvTickets.adapter = adapter

                binding.progressBar.visibility = ProgressBar.INVISIBLE
            }

            binding.swipeContainer.setOnRefreshListener {
                binding.swipeContainer.isRefreshing = false
            }

        }

        return binding.root
    }

}
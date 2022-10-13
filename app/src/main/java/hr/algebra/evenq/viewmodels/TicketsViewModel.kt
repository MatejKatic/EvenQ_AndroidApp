package hr.algebra.evenq.viewmodels

import android.app.Application
import androidx.lifecycle.*
import hr.algebra.evenq.database.TicketDatabase
import hr.algebra.evenq.database.TicketRepository
import hr.algebra.evenq.database.model.AdminTicketDb
import hr.algebra.evenq.database.model.TicketDb
import hr.algebra.evenq.network.Network
import hr.algebra.evenq.network.model.Ticket
import kotlinx.coroutines.launch

class TicketsViewModel(application: Application): AndroidViewModel(application) {

    val ticketsList = MutableLiveData<ArrayList<Ticket>>()
    val ticketsFromDb: LiveData<List<TicketDb>>
    private val repository: TicketRepository

    init {
        val ticketdao = TicketDatabase.getDatabase(application)!!.ticketDao()
        repository = TicketRepository(ticketdao)
        ticketsFromDb = repository.readAllTickets
    }

    fun getTicketsForMember(memberId: String){
        viewModelScope.launch {
            ticketsList.value = Network().getService().getTicketsByMemberId(memberId)
        }
    }

    fun insertAllTickets(list: ArrayList<Ticket>) {
        val listToInsert = ArrayList<TicketDb>()
        list.forEach { listToInsert.add(it.convertToTicketDb()) }
        viewModelScope.launch {
            repository.insertAllTickets(listToInsert)
        }
    }

}
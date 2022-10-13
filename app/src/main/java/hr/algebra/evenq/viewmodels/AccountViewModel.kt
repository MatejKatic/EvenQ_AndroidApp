package hr.algebra.evenq.viewmodels

import android.app.Application
import androidx.lifecycle.*
import hr.algebra.evenq.database.TicketDatabase
import hr.algebra.evenq.database.TicketRepository
import hr.algebra.evenq.database.model.AdminTicketDb
import hr.algebra.evenq.database.model.TicketDb
import hr.algebra.evenq.network.Network
import hr.algebra.evenq.network.model.Member
import hr.algebra.evenq.network.model.Ticket
import kotlinx.coroutines.launch

class AccountViewModel(application: Application): AndroidViewModel(application) {

    val memberAccount = MutableLiveData<Member>()
    val ticketsList = MutableLiveData<ArrayList<Ticket>>()
    val ticketsFromDb: LiveData<List<AdminTicketDb>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDatabase.getDatabase(application)!!.ticketDao()
        repository = TicketRepository(ticketDao)
        ticketsFromDb = repository.readAllAdminTickets
    }

    fun getAllTickets() {
        viewModelScope.launch {
            val ticketsList = Network().getService().getAllTickets()
            val ticketsToSave = ArrayList<AdminTicketDb>()
            ticketsList.forEach {
                ticketsToSave.add(it.convertToAdminTicket())
            }
            repository.insertAllAdminTickets(ticketsToSave)
        }
    }

    fun getMemberAccountInfo(memberId: String) {
        viewModelScope.launch {
            memberAccount.value = Network().getService().getMemberInfo(memberId)
        }
    }

}
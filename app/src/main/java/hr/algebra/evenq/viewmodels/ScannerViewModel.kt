package hr.algebra.evenq.viewmodels

import android.app.Application
import androidx.lifecycle.*
import hr.algebra.evenq.database.TicketDatabase
import hr.algebra.evenq.database.TicketRepository
import hr.algebra.evenq.database.model.AdminTicketDb
import hr.algebra.evenq.network.Network
import hr.algebra.evenq.network.model.Ticket
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ScannerViewModel(application: Application): AndroidViewModel(application){

    val ticketsFromDb: LiveData<List<AdminTicketDb>>
    private val repository: TicketRepository

    init {
        val ticketDao = TicketDatabase.getDatabase(application)!!.ticketDao()
        repository = TicketRepository(ticketDao)
        ticketsFromDb = repository.readAllAdminTickets
    }

    fun updateTicket(ticketId: Int, requestBody: RequestBody) {
        viewModelScope.launch {
            Network().getService().updateTicket(ticketId, requestBody)
        }
    }

}
package hr.algebra.evenq.database

import hr.algebra.evenq.database.model.AdminTicketDb
import hr.algebra.evenq.database.model.TicketDb

class TicketRepository(private val ticketDao: TicketDao) {

    val readAllTickets = ticketDao.getTickets()
    val readAllAdminTickets = ticketDao.getAllAdminTickets()

    fun insertAllTickets(list: ArrayList<TicketDb>){
        ticketDao.insertAllTickets(list)
    }

    fun insertAllAdminTickets(tickets: ArrayList<AdminTicketDb>) {
        ticketDao.insertAllAdminTickets(tickets)
    }

}
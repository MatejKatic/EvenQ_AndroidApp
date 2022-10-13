package hr.algebra.evenq.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import hr.algebra.evenq.network.model.Event
import hr.algebra.evenq.network.model.Member
import hr.algebra.evenq.network.model.Ticket
import java.io.Serializable

@Entity(tableName = "adminTickets")
class AdminTicketDb(
    @PrimaryKey
    val idTicket: Int,
    val ticketQR: String,
    val member: Member,
    val memberId: String,
    val event: Event,
    val eventId: Int,
    var isValid: Boolean
) : Serializable {

    fun convertToTicket(): Ticket {
        return Ticket(
            this.idTicket,
            this.ticketQR,
            this.member,
            this.memberId,
            this.event,
            this.eventId,
            this.isValid
        )
    }
}
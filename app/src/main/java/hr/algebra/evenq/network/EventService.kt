package hr.algebra.evenq.network

import hr.algebra.evenq.network.model.Event
import hr.algebra.evenq.network.model.Member
import hr.algebra.evenq.network.model.Ticket
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    @GET("Event/UpcomingEvents")
    suspend fun getUpcomingEvents(): ArrayList<Event>

    @GET("Ticket/{memberId}")
    suspend fun getTicketsByMemberId(@Path("memberId") memberId: String): ArrayList<Ticket>

    @GET("Ticket")
    suspend fun getAllTickets(): ArrayList<Ticket>

    @GET("Member/{memberId}")
    suspend fun getMemberInfo(@Path("memberId") memberId: String): Member

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("Ticket/{ticketId}")
    suspend fun updateTicket(
        @Path("ticketId") ticketId: Int, @Body requestBody: RequestBody
    ): Response<ResponseBody>
}
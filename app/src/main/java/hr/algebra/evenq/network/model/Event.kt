package hr.algebra.evenq.network.model

import java.io.Serializable

data class Event(
    val idEvent: Int,
    val title: String,
    val description: String,
    val posterImage: String?,
    val date: String,
    val location: Location?,
    val locationId: Int,
    val ticketPrice: Double
): Serializable {
}
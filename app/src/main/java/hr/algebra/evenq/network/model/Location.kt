package hr.algebra.evenq.network.model

import java.io.Serializable

data class Location(
    val idLocation: Int,
    val city: String,
    val street: String,
    val coordinates: String
): Serializable {
}
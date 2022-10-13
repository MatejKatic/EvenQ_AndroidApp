package hr.algebra.evenq.network.model

import java.io.Serializable

data class Member(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val refferalCode: String,
    val isAdmin: Boolean,
    val numberOfRefferals: Int,
    val membershipValid: Boolean
): Serializable {
}
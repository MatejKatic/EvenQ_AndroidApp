package hr.algebra.evenq.network.model

import hr.algebra.evenq.framework.formatDate
import java.util.*

data class Message(
    val senderId: String = "",
    val message: String = "",
    val senderName: String = "",
    val dateTime: String = Date().formatDate()
)

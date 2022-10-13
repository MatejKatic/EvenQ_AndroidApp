package hr.algebra.evenq.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import hr.algebra.evenq.R
import hr.algebra.evenq.framework.parseDate
import hr.algebra.evenq.network.model.Message
import org.ocpsoft.prettytime.PrettyTime


class MessageAdapter(private val context: Context, private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SENT = 0
    private val RECEIVE = 1

    class SentMessageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sentMessage: TextView = itemView.findViewById(R.id.textMessage)
        private val dateTime: TextView = itemView.findViewById(R.id.msgDateTime)

        fun bind(message: Message) {
            sentMessage.text = message.message
            dateTime.text = PrettyTime().format(parseDate(message.dateTime))

        }
    }

    class ReceivedMessageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sentMessage: TextView = itemView.findViewById(R.id.textMessage)
        private val dateTime: TextView = itemView.findViewById(R.id.msgDateTime)
        private val profileName: TextView = itemView.findViewById(R.id.profileName)

        fun bind(message: Message) {
            sentMessage.text = message.message
            profileName.text = message.senderName
            dateTime.text = PrettyTime().format(parseDate(message.dateTime))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            RECEIVE -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
                return ReceivedMessageVH(view)
            }
            SENT -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
                return SentMessageVH(view)
            }
        }
        throw Exception("Fatal error.")
    }

    override fun getItemViewType(position: Int) =
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(messages[position].senderId)) SENT else RECEIVE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.javaClass) {
            SentMessageVH::class.java -> {
                (holder as SentMessageVH).bind(messages[position])
            }
            ReceivedMessageVH::class.java -> {
                (holder as ReceivedMessageVH).bind(messages[position])
            }
        }
    }

    override fun getItemCount() = messages.size
}
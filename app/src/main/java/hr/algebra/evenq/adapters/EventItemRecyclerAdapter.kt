package hr.algebra.evenq.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.evenq.ChatActivity
import hr.algebra.evenq.EventMapsActivity
import hr.algebra.evenq.R
import hr.algebra.evenq.databinding.EventItemViewBinding
import hr.algebra.evenq.network.model.Event
import java.time.LocalDateTime

const val EXTRA_EVENT = "hr.algebra.evenq.extraEvent"
const val EXTRA_EVENT_NAME = "hr.algebra.evenq.extraEventName"
const val EXTRA_EVENT_ID = "hr.algebra.evenq.extraEventID"

class EventItemRecyclerAdapter(
    private val context: Context,
    private val eventsList: ArrayList<Event>
) : RecyclerView.Adapter<EventItemRecyclerAdapter.EventItemViewHolder>() {

    class EventItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = EventItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.event_item_view, parent, false)
        return EventItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        val event = eventsList[position]

        holder.binding.tvEventTitle.text = event.title

        val date = LocalDateTime.parse(event.date)

        holder.binding.tvEventDate.text =
            date.dayOfWeek.toString() + ", " + date.dayOfMonth + "." + date.monthValue.toString() + "." +
                    " " + date.hour.toString() + ":00"

        if (event.description.isNotEmpty()) {
            if (event.description.length > 58) {
                holder.binding.tvEventDescription.text = event.description.take(58) + "..."
            } else {
                holder.binding.tvEventDescription.text = event.description + "..."
            }
        }

        if (event.posterImage != null) {
            try {
                val bytes = Base64.decode(event.posterImage, Base64.DEFAULT)
                holder.binding.ivEventImage.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )
                )
            } catch (e: Exception) {
                holder.binding.ivEventImage.setImageResource(R.drawable.ic_error)
            }
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, EventMapsActivity::class.java).apply {
                putExtra(EXTRA_EVENT, event)
            }
            context.startActivity(intent)
        }

        holder.binding.btnChat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_EVENT_NAME, event.title)
                putExtra(EXTRA_EVENT_ID, event.idEvent.toString())
            }
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return eventsList.size
    }

}
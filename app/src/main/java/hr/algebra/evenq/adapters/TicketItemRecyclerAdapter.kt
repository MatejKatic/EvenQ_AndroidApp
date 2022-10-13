package hr.algebra.evenq.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import hr.algebra.evenq.R
import hr.algebra.evenq.databinding.TicketItemViewBinding
import hr.algebra.evenq.network.model.Ticket
import java.time.LocalDateTime

class TicketItemRecyclerAdapter(
    private val context: Context,
    private val ticketsList: ArrayList<Ticket>
): RecyclerView.Adapter<TicketItemRecyclerAdapter.TicketItemViewHolder>() {

    class TicketItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = TicketItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ticket_item_view, parent, false)
        return TicketItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        val ticket = ticketsList[position]

        try {
            val bitMatrix = QRCodeWriter().encode(ticket.ticketQR, BarcodeFormat.QR_CODE, 300, 300)
            val bmp = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
            for (x in 0 until bitMatrix.width){
                for (y in 0 until bitMatrix.height){
                    bmp.setPixel(x, y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            holder.binding.ivTicketCode.setImageBitmap(bmp)
            holder.binding.tvTicketEventTitle.text = ticket.event.title

            val date = LocalDateTime.parse(ticket.event.date)

            holder.binding.tvTicketEventDate.text = date.dayOfMonth.toString() + "." + date.monthValue.toString() + "." +
                        date.year.toString() + " " + date.hour.toString() + ":00"
        } catch (e: WriterException){

        }
    }

    override fun getItemCount(): Int {
        return ticketsList.size
    }

}
package hr.algebra.evenq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import hr.algebra.evenq.adapters.EXTRA_EVENT_ID
import hr.algebra.evenq.databinding.ActivityScannerBinding
import hr.algebra.evenq.network.model.Ticket
import hr.algebra.evenq.viewmodels.ScannerViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var codeScanner: CodeScanner

    private val viewModel: ScannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedEventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

        requestCamera.launch(android.Manifest.permission.CAMERA)

        codeScanner = CodeScanner(this, binding.scannerView).apply {
            this.camera = CodeScanner.CAMERA_BACK
            this.formats = CodeScanner.ALL_FORMATS
            this.autoFocusMode = AutoFocusMode.SAFE
            this.scanMode = ScanMode.SINGLE
            this.isAutoFocusEnabled = true
            this.isFlashEnabled = false
        }

        viewModel.ticketsFromDb.observe(this) { ticketList ->
            codeScanner.decodeCallback = DecodeCallback { result ->
                val ticketToUpdate = ticketList.firstOrNull { it.ticketQR == result.text }

                runOnUiThread(Runnable {
                    if (ticketToUpdate != null && ticketToUpdate.isValid && ticketToUpdate.eventId == selectedEventId) {
                        Toast.makeText(this, "Ticket Valid", Toast.LENGTH_SHORT).show()
                        ticketToUpdate.isValid = false
                        viewModel.updateTicket(ticketToUpdate.idTicket,
                            generateTicketRequestBody(ticketToUpdate.convertToTicket()))
                        codeScanner.startPreview()
                    }  else {
                        Toast.makeText(this, "Ticket Invalid", Toast.LENGTH_SHORT).show()
                        codeScanner.startPreview()
                    }
                })
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    private fun generateTicketRequestBody(ticket: Ticket): RequestBody {
        val jsonObject = JSONObject()
        val memberJsonObject = JSONObject()
        val eventJsonObject = JSONObject()

        memberJsonObject.put("uid", ticket.member.uid)
        memberJsonObject.put("firstName", ticket.member.firstName)
        memberJsonObject.put("lastName", ticket.member.lastName)
        memberJsonObject.put("refferalCode", ticket.member.refferalCode)
        memberJsonObject.put("isAdmin", ticket.member.isAdmin)
        memberJsonObject.put("numberOfRefferals", ticket.member.numberOfRefferals)
        memberJsonObject.put("membershipValid", ticket.member.membershipValid)

        eventJsonObject.put("idEvent", ticket.event.idEvent)
        eventJsonObject.put("title", ticket.event.title)
        eventJsonObject.put("description", ticket.event.description)
        eventJsonObject.put("posterImage", ticket.event.posterImage)
        eventJsonObject.put("date", ticket.event.date)
        eventJsonObject.put("location", ticket.event.location)
        eventJsonObject.put("locationId", ticket.event.locationId)
        eventJsonObject.put("ticketPrice", ticket.event.ticketPrice)

        jsonObject.put("idTicket", ticket.idTicket)
        jsonObject.put("ticketQR", ticket.ticketQR)
        jsonObject.put("member", memberJsonObject)
        jsonObject.put("memberId", ticket.memberId)
        jsonObject.put("event", eventJsonObject)
        jsonObject.put("eventId", ticket.eventId)
        jsonObject.put("isValid", ticket.isValid)

        val jsonObjectString = jsonObject.toString()

        return jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
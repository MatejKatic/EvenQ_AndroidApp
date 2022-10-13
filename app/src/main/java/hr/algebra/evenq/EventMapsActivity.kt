package hr.algebra.evenq

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.evenq.adapters.EXTRA_EVENT
import hr.algebra.evenq.adapters.EXTRA_EVENT_ID
import hr.algebra.evenq.adapters.EXTRA_EVENT_NAME
import hr.algebra.evenq.databinding.ActivityEventMapsBinding
import hr.algebra.evenq.network.model.Event
import hr.algebra.evenq.viewmodels.AccountViewModel
import java.time.LocalDateTime

class EventMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityEventMapsBinding

    private lateinit var selectedEvent: Event
    private val viewModel: AccountViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.eventActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        viewModel.memberAccount.observe(this) { member ->
            if(member.isAdmin) {
                binding.btnScanTicket.visibility = View.VISIBLE
                viewModel.getAllTickets()
            }
        }

        selectedEvent = intent.getSerializableExtra(EXTRA_EVENT) as Event
        val date = LocalDateTime.parse(selectedEvent.date)

        if (selectedEvent.posterImage != null) {
            try {
                val bytes = Base64.decode(selectedEvent.posterImage, Base64.DEFAULT)
                binding.ivEventImage.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size
                    )
                )
            } catch (e: Exception) {
                binding.ivEventImage.setImageResource(R.drawable.ic_error)
            }
        }

        setEventDetails(date)
        setButtonListeners()
        viewModel.getMemberAccountInfo(mAuth.currentUser!!.uid)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setButtonListeners() {
        binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra(EXTRA_EVENT_NAME, selectedEvent.title)
                putExtra(EXTRA_EVENT_ID, selectedEvent.idEvent.toString())
            }
            this.startActivity(intent)
        }

        binding.btnScanTicket.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java).apply {
                putExtra(EXTRA_EVENT_ID, selectedEvent.idEvent)
            }
            this.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setEventDetails(date: LocalDateTime) {
        binding.tvEventName.text = selectedEvent.title
        binding.tvEventLocation.text = selectedEvent.location?.street + ", " + selectedEvent.location?.city
        binding.tvEventDate.text =
            date.dayOfWeek.toString() + ", " + date.dayOfMonth + "." + date.monthValue.toString() + "." +
                    " " + date.hour.toString() + ":00"

        binding.tvEventDescription.text = selectedEvent.description
        binding.tvTicketPrice.text = selectedEvent.ticketPrice.toString() + "€"
        binding.tvCityName.text = selectedEvent.location?.city
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val coordinates = selectedEvent.location?.coordinates!!.split(',')
        val mapLocation = LatLng(coordinates[0].toDouble(), coordinates[1].toDouble())
        val zoomLevel = 15f

        mMap.addMarker(MarkerOptions().position(mapLocation).title(selectedEvent.location?.street))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, zoomLevel))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
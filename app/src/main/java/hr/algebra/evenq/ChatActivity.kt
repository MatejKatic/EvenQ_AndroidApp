package hr.algebra.evenq

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import hr.algebra.evenq.adapters.EXTRA_EVENT_ID
import hr.algebra.evenq.adapters.EXTRA_EVENT_NAME
import hr.algebra.evenq.adapters.MessageAdapter
import hr.algebra.evenq.databinding.ActivityChatBinding
import hr.algebra.evenq.network.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var eventID: String

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mDatabase: DatabaseReference

    private lateinit var messages: ArrayList<Message>


    private val CHAT = "chat"
    private val MESSAGE = "message"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messages = ArrayList()
        mDatabase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()


        eventID = intent.getStringExtra(EXTRA_EVENT_ID).toString()
        binding.tvEventName.text = intent.getStringExtra(EXTRA_EVENT_NAME).toString()

        setListeners()


    }

    private fun setListeners() {
        mDatabase.child(CHAT).child(eventID).child(MESSAGE).limitToLast(20)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (postSnapshot in snapshot.children) {
                        messages.add(postSnapshot.getValue(Message::class.java)!!)
                    }

                    binding.rvMessages.apply {
                        layoutManager = LinearLayoutManager(context).apply {
                            stackFromEnd = true
                        }
                        adapter = MessageAdapter(context, messages)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DB_ERROR", "DB error")
                }
            })

        binding.sendButton.setOnClickListener {

            if (binding.messageBox.text.trim().isNotEmpty()) {

                mDatabase.child(CHAT).child(eventID).child(MESSAGE).push()
                    .setValue(
                        Message(
                            mAuth.currentUser!!.uid,
                            binding.messageBox.text.trim().toString(),
                            mAuth.currentUser!!.email.let {
                                it!!.substring(0, it.lastIndexOf("@"))
                            }
                        )
                    ).addOnSuccessListener {
                        binding.messageBox.text.clear()
                    }
            }
        }
    }

}
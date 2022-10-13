package hr.algebra.evenq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.evenq.databinding.ActivityMainBinding
import hr.algebra.evenq.fragments.AccountFragment
import hr.algebra.evenq.fragments.EventsFragment
import hr.algebra.evenq.fragments.TicketsFragment

private lateinit var binding: ActivityMainBinding
private lateinit var mAuth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        setupBottomNavigationListeners()
        replaceFragment(EventsFragment())

    }


    private fun setupBottomNavigationListeners(){
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.icon_events -> replaceFragment(EventsFragment())
                R.id.icon_tickets -> replaceFragment(TicketsFragment())
                R.id.icon_account -> replaceFragment(AccountFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
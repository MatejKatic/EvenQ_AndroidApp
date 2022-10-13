package hr.algebra.evenq.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.evenq.R
import hr.algebra.evenq.SignInActivity
import hr.algebra.evenq.databinding.FragmentAccountBinding
import hr.algebra.evenq.network.model.Member
import hr.algebra.evenq.viewmodels.AccountViewModel

private lateinit var mAuth: FirebaseAuth

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()

        viewModel.memberAccount.observe(viewLifecycleOwner) { member ->
            binding.progressBar.visibility = ProgressBar.VISIBLE
            setupAccountDetails(member)
            binding.progressBar.visibility = ProgressBar.GONE
        }


        setLogoutListener()

        viewModel.getMemberAccountInfo(mAuth.currentUser!!.uid)
        return binding.root
    }

    private fun setupAccountDetails(member: Member) {
        binding.tvTitle.text = getString(R.string.account_details)
        binding.tvFirstNameLabel.text = getString(R.string.first_name)
        binding.tvLastNameLabel.text = getString(R.string.last_name)
        binding.tvRefferalLabel.text = getString(R.string.refferal_code)
        binding.tvMembershipLabel.text = getString(R.string.membership)

        binding.tvFirstName.text = member.firstName
        binding.tvLastName.text = member.lastName
        binding.tvRefferal.text = member.refferalCode
        if (member.membershipValid) {
            binding.tvMembership.text = getString(R.string.valid)
            binding.tvMembership.setTextColor(requireContext().getColor(R.color.valid))
        } else {
            binding.tvMembership.text = getString(R.string.expired)
            binding.tvMembership.setTextColor(requireContext().getColor(R.color.expired))
        }
    }

    private fun setLogoutListener() {
        binding.btnLogout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            requireContext().startActivity(intent)
            requireActivity().finish()
        }
    }

}
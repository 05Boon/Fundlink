package com.example.fundlink.ui.officer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fundlink.R
import com.example.fundlink.data.repository.AuthRepository
import com.example.fundlink.data.repository.OfficerRepository
import com.example.fundlink.databinding.FragmentOfficerDashboardBinding
import kotlinx.coroutines.launch

class OfficerDashboardFragment : Fragment(R.layout.fragment_officer_dashboard) {
    private var _binding: FragmentOfficerDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentOfficerDashboardBinding.bind(view)
        val auth = AuthRepository(requireContext())
        val repo = OfficerRepository(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val user = auth.currentUser()
            val total = repo.totalOpportunities()
            val pending = repo.pendingCount()
            _binding?.apply {
                tvOfficerWelcome.text = "Welcome, ${user?.fullName ?: "Officer"}"
                tvTotalOpportunities.text = "Active opportunities: $total"
                tvPendingApplications.text = "Pending applications: $pending"
            }
        }

        binding.btnManageOpportunities.setOnClickListener {
            findNavController().navigate(R.id.action_officerDashboardFragment_to_manageOpportunitiesFragment)
        }

        binding.btnReviewApplications.setOnClickListener {
            findNavController().navigate(R.id.action_officerDashboardFragment_to_reviewApplicationsFragment)
        }

        binding.btnOfficerLogout.setOnClickListener {
            auth.logout()
            findNavController().navigate(R.id.action_global_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
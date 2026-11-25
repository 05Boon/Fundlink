package com.example.fundlink.ui.officer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fundlink.R
import com.example.fundlink.data.model.FundingOpportunity
import com.example.fundlink.data.repository.OfficerRepository
import com.example.fundlink.databinding.FragmentManageOpportunitiesBinding
import kotlinx.coroutines.launch

class ManageOpportunitiesFragment : Fragment(R.layout.fragment_manage_opportunities) {
    private var _binding: FragmentManageOpportunitiesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentManageOpportunitiesBinding.bind(view)
        val repo = OfficerRepository(requireContext())

        binding.btnSaveOpportunity.setOnClickListener {
            val title = binding.etOpportunityTitle.text.toString().trim()
            val desc = binding.etOpportunityDescription.text.toString().trim()
            val amount = binding.etOpportunityAmount.text.toString().toDoubleOrNull()
            val deadline = binding.etOpportunityDeadline.text.toString().trim()
            val eligibility = binding.etOpportunityEligibility.text.toString().trim()

            if (title.isBlank() || desc.isBlank() || amount == null || amount <= 0 ||
                deadline.isBlank() || eligibility.isBlank()
            ) {
                Toast.makeText(requireContext(), "Complete all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                runCatching {
                    repo.createOpportunity(
                        FundingOpportunity(
                            title = title,
                            description = desc,
                            amount = amount,
                            deadline = deadline,
                            eligibility = eligibility
                        )
                    )
                }.onSuccess {
                    Toast.makeText(requireContext(), "Opportunity published", Toast.LENGTH_SHORT).show()
                    _binding?.apply {
                        etOpportunityTitle.text?.clear()
                        etOpportunityDescription.text?.clear()
                        etOpportunityAmount.text?.clear()
                        etOpportunityDeadline.text?.clear()
                        etOpportunityEligibility.text?.clear()
                    }
                }.onFailure {
                    Toast.makeText(requireContext(), it.message ?: "Save failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
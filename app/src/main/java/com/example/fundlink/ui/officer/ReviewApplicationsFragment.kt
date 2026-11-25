package com.example.fundlink.ui.officer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundlink.R
import com.example.fundlink.data.repository.OfficerRepository
import com.example.fundlink.databinding.FragmentReviewApplicationsBinding
import com.example.fundlink.ui.adapters.ReviewAdapter
import kotlinx.coroutines.launch

class ReviewApplicationsFragment : Fragment(R.layout.fragment_review_applications) {
    private var _binding: FragmentReviewApplicationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: OfficerRepository
    private lateinit var adapter: ReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentReviewApplicationsBinding.bind(view)
        repo = OfficerRepository(requireContext())
        adapter = ReviewAdapter { item, status, comment ->
            viewLifecycleOwner.lifecycleScope.launch {
                runCatching { repo.decide(item.id, status, comment) }
                    .onSuccess {
                        Toast.makeText(
                            requireContext(),
                            "Application ${status.lowercase()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        load()
                    }
                    .onFailure {
                        Toast.makeText(
                            requireContext(),
                            it.message ?: "Decision failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }

        binding.recyclerReviewApplications.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReviewApplications.adapter = adapter
        load()
    }

    private fun load() {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching { repo.applications() }.onSuccess { list ->
                _binding?.let {
                    adapter.submit(list)
                    it.tvNoApplications.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.fundlink.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundlink.data.model.StudentApplication
import com.example.fundlink.databinding.ItemReviewApplicationBinding

class ReviewAdapter(
    private val onDecide: (StudentApplication, String, String) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.Holder>() {

    private val items = mutableListOf<StudentApplication>()

    fun submit(newItems: List<StudentApplication>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): Holder =
        Holder(ItemReviewApplicationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(items[position])

    inner class Holder(private val b: ItemReviewApplicationBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(a: StudentApplication) {
            b.tvApplicantName.text = a.studentName
            b.tvApplicationOpportunity.text = a.opportunityTitle
            b.tvRequestedAmount.text = "Requested: KES %,.2f".format(a.requestedAmount)
            b.tvApplicationStatement.text = a.statement

            val pending = a.status == "PENDING"
            b.tvApplicationStatus.text = if (pending || a.reviewComment.isBlank()) {
                "Status: ${a.status}"
            } else {
                "Status: ${a.status} — ${a.reviewComment}"
            }
            b.etReviewComment.setText("")
            b.etReviewComment.visibility = if (pending) View.VISIBLE else View.GONE
            b.layoutDecisionButtons.visibility = if (pending) View.VISIBLE else View.GONE

            b.btnApprove.setOnClickListener {
                onDecide(a, "APPROVED", b.etReviewComment.text.toString())
            }
            b.btnReject.setOnClickListener {
                onDecide(a, "REJECTED", b.etReviewComment.text.toString())
            }
        }
    }
}
package com.example.fundlink.data.repository

import android.content.Context
import com.example.fundlink.data.db.AppDatabase
import com.example.fundlink.data.model.FundingOpportunity
import com.example.fundlink.data.model.StudentApplication
import com.example.fundlink.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfficerRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val session = SessionManager(context)

    private fun requireOfficer() {
        if (session.role() != "OFFICER") error("Officer access required")
    }

    suspend fun createOpportunity(item: FundingOpportunity): Long =
        withContext(Dispatchers.IO) {
            requireOfficer()
            require(item.title.isNotBlank() && item.amount > 0)
            db.opportunityDao().insert(item.copy(createdBy = session.userId(), active = true))
        }

    suspend fun applications(): List<StudentApplication> = withContext(Dispatchers.IO) {
        requireOfficer()
        db.applicationDao().all()
    }

    suspend fun decide(id: Long, status: String, comment: String) =
        withContext(Dispatchers.IO) {
            requireOfficer()
            require(status == "APPROVED" || status == "REJECTED")
            val rows = db.applicationDao().decide(
                id, status, session.userId(), System.currentTimeMillis(), comment.trim()
            )
            if (rows == 0) error("Application already reviewed")
        }

    suspend fun totalOpportunities(): Int = withContext(Dispatchers.IO) {
        db.opportunityDao().activeCount()
    }

    suspend fun pendingCount(): Int = withContext(Dispatchers.IO) {
        db.applicationDao().pendingCount()
    }
}
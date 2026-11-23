package com.example.fundlink.data.repository

import android.content.Context
import com.example.fundlink.data.db.AppDatabase
import com.example.fundlink.data.model.User
import com.example.fundlink.data.security.PasswordHasher
import com.example.fundlink.data.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) {
    private val userDao = AppDatabase.getInstance(context).userDao()
    private val session = SessionManager(context)

    suspend fun currentUser(): User? = withContext(Dispatchers.IO) {
        val uid = session.userId()
        if (uid == -1L) null else userDao.findById(uid)
    }

    fun logout() {
        session.clear()
    }

    suspend fun seedOfficer() {
        if (userDao.findByEmail("officer@fundlink.com") == null) {
            val salt = PasswordHasher.newSalt()
            val hash = PasswordHasher.hash("Officer@123", salt)
            userDao.insert(
                User(
                    email = "officer@fundlink.com",
                    passwordHash = hash,
                    salt = salt,
                    fullName = "Funding Officer",
                    role = "OFFICER"
                )
            )
        }
    }
}

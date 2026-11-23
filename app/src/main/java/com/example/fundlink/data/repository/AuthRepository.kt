package com.example.fundlink.data.repository

import android.content.Context
import com.example.fundlink.data.db.AppDatabase
import com.example.fundlink.data.model.User
import com.example.fundlink.data.security.PasswordHasher

class AuthRepository(context: Context) {
    private val userDao = AppDatabase.getInstance(context).userDao()

    suspend fun seedOfficer() {
        if (userDao.findByEmail("officer@fundlink.com") == null) {
            val salt = PasswordHasher.newSalt()
            val hash = PasswordHasher.hash("Officer@123", salt)
            userDao.insert(
                User(
                    email = "officer@fundlink.com",
                    passwordHash = hash,
                    salt = salt,
                    role = "OFFICER"
                )
            )
        }
    }
}

    package com.example.myproject.data.model

    data class User(
        val id: Long,
        val email: String,
        val role: UserRole,
        val validated: Boolean,
        val username: String,
        val location: String?,
        val phoneNumber: String?,
        val documentUrl: String? // si merchant ou association
    )

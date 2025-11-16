package com.example.myproject.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val role: UserRole,
    val username: String,
    val location: String?,
    val phoneNumber: String?,
    val documentUrl: String? // seulement pour merchant/association
)

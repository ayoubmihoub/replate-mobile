package com.example.myproject.data.model

data class AuthResponse(
    val id: Long,
    val email: String,
    val role: UserRole,
    val token: String
)

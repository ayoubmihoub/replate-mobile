package com.example.myproject.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    // Correspond au champ 'userId' du JSON
    @SerializedName("userId")
    val id: Long,

    // Correspond au champ 'email' du JSON
    val email: String,

    // Correspond au champ 'role' du JSON
    val role: UserRole,

    // Correspond au champ 'jwtToken' du JSON
    @SerializedName("jwtToken")
    val token: String,

    @SerializedName("isVerified")
    val isVerified: Boolean
)
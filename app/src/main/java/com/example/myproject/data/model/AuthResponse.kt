package com.example.myproject.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("userId")
    val id: Long,

    val email: String?,

    // Rendre le rôle nullable pour éviter le crash si le mapping échoue
    val role: UserRole?,

    @SerializedName("jwtToken")
    val token: String?,

    // On garde les deux possibilités pour être sûr de capter la validation
    @SerializedName(value = "isValidated", alternate = ["isVerified", "validated", "verified"])
    val isValidated: Boolean = false
)
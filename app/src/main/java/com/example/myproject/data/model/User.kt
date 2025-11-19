    package com.example.myproject.data.model

    import com.google.gson.annotations.SerializedName

    data class User(
        val id: Long,
        val email: String,
        val role: UserRole,
        @SerializedName("validatedByAdmin") // ou "isValidated", vérifiez le JSON brut !
        val isValidatedByAdmin: Boolean = false,
        val username: String,
        val location: String?,
        val phoneNumber: String?,
        val documentUrl: String? // si merchant ou association
    )

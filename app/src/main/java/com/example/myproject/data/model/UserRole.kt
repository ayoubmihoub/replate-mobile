package com.example.myproject.data.model

import com.google.gson.annotations.SerializedName

enum class UserRole {
    @SerializedName("INDIVIDUAL", alternate = ["individual", "Individual"])
    INDIVIDUAL,

    @SerializedName("MERCHANT", alternate = ["merchant", "Merchant"])
    MERCHANT,

    @SerializedName("ASSOCIATION", alternate = ["association", "Association"])
    ASSOCIATION,

    @SerializedName("ADMIN", alternate = ["admin", "Admin"])
    ADMIN
}
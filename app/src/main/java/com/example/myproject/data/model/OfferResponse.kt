package com.example.myproject.data.model

data class OfferResponse(
    val id: Long,
    val foodTitle: String,
    val message: String = "Annonce créée avec succès"
)
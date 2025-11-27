package com.example.myproject.data.model

data class OfferRequest(
    val foodTitle: String,
    val quantity: String, // Correspond à inputQuantity
    val expirationDate: String, // Format "dd/MM/yyyy"
    val contactNumber: String,
    val pickupAddress: String,
    // La gestion des fichiers est complexe (Multi-part). Pour la simplicité de la
    // requête JSON, nous supposons que vous envoyez l'URL de l'image après un pré-upload.
    val imageUrl: String? = null
)
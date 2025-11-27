package com.example.myproject.data.model

// Modèle de DTO pour POST /offers/create
data class AnnouncementRequest(
    val title: String,
    val description: String,
    // Les types correspondent au backend
    val price: Double? = 0.0, // Valeur par défaut si non fourni par l'UI
    val announcementType: AnnouncementType = AnnouncementType.DONATION, // Valeur par défaut
    val imageUrl1: String? = null,
    // NOTE TECHNIQUE: Le backend attend un LocalDateTime.
    // Nous envoyons ici le String formaté "dd/MM/yyyy" par l'UI.
    // Assurez-vous que Retrofit ou votre backend gère la conversion de ce format de String vers LocalDateTime.
    val expiryDate: String
)
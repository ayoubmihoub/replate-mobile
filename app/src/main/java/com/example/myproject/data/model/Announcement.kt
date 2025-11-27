package com.example.myproject.data.model

// Modèle de réponse pour GET /offers/my-offers
data class Announcement(
    val id: Long,
    val merchantId: Long,
    val title: String,
    val description: String?,
    // Les types correspondent au backend
    val announcementType: AnnouncementType,
    val moderationStatus: ModerationStatus,
    val price: Double?,
    val imageUrl1: String?,
    // Les dates seront probablement reçues comme des Strings ISO-8601 (ou autre)
    val expiryDate: String,
    val createdAt: String,
    val updatedAt: String
)
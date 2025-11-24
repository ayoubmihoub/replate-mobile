package com.example.myproject.models // Changez le chemin du package au besoin

// La classe de données elle-même
data class Transaction(
    val senderName: String,
    val entityType: String,
    val announcementTitle: String,
    val status: String, // "Pending", "Accepted", "Rejected"
    val date: String,
    val canAct: Boolean // Indique si les boutons Rejeter/Accepter doivent être affichés
)
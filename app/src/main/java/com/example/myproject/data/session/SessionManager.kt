package com.example.myproject.data.session

// ⚠️ ATTENTION : Ces valeurs sont pour le TEST SEULEMENT et doivent être remplacées
// par votre logique de stockage réelle après le login.
object SessionManager {

    // Simuler un ID utilisateur Marchand (Long)
    // Le backend utilise Long pour l'ID [cite: package com.replate.offermanagementservice.controller;]
    private const val TEST_USER_ID = 1L

    // Simuler le Rôle (doit être "MERCHANT" pour créer des annonces)
    private const val TEST_USER_ROLE = "MERCHANT"

    // Simuler le statut Validé (doit être true pour créer une annonce)
    private const val TEST_IS_VALIDATED = true

    /**
     * Récupère l'ID de l'utilisateur.
     * REMPLACER par la récupération depuis SharedPreferences/DataStore.
     */
    fun getUserId(): Long {
        // Force le retour d'un Long non-nullable pour les besoins du test
        // NOTE: Dans une vraie application, cette fonction pourrait retourner Long?
        return TEST_USER_ID
    }

    /**
     * Récupère le Rôle de l'utilisateur.
     * REMPLACER par la récupération depuis SharedPreferences/DataStore.
     */
    fun getUserRole(): String {
        return TEST_USER_ROLE
    }

    /**
     * Récupère le statut de validation.
     * REMPLACER par la récupération depuis SharedPreferences/DataStore.
     */
    fun isValidated(): Boolean {
        return TEST_IS_VALIDATED
    }

    // NOTE : Vous auriez aussi besoin d'une fonction pour stocker ces valeurs après /users/login
    // fun saveSession(userId: Long, role: String, isValidated: Boolean) { ... }
}
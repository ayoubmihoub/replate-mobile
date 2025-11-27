package com.example.myproject.data.session

// ⚠️ ATTENTION : Ces valeurs sont pour le TEST SEULEMENT et doivent être remplacées
// par votre logique de stockage réelle après le login.
object SessionManager {

    // ⭐ NOUVEAU : Variable interne pour le jeton JWT (variable d'état)
    private var authToken: String? = null

    // Simuler un ID utilisateur Marchand (Long)
    // REMPLACER par une gestion dynamique.
    private const val TEST_USER_ID = 1L
    private const val TEST_USER_ROLE = "MERCHANT"
    private const val TEST_IS_VALIDATED = true

    /**
     * ⭐ NOUVEAU : Sauvegarde le jeton JWT en mémoire pour la durée de la session de l'application.
     * C'est la méthode que LoginActivity va appeler.
     */
    fun saveAuthToken(token: String?) {
        authToken = token
    }

    /**
     * Récupère le jeton JWT. Utilisé par les Repositories (ex: AnnouncementRepository).
     */
    fun getAuthToken(): String? {
        // Retourne le jeton qui a été sauvé par LoginActivity
        return authToken
    }

    // ⭐ NOUVEAU : Nettoie l'état du SessionManager (optionnel mais recommandé lors de la déconnexion/reconnexion)
    fun clearInMemorySession() {
        authToken = null
        // Vous pouvez ajouter ici la réinitialisation d'autres champs de session si nécessaire
    }

    /**
     * Récupère l'ID de l'utilisateur.
     * REMPLACER par la récupération depuis SharedPreferences/DataStore.
     */
    fun getUserId(): Long {
        // Force le retour d'un Long non-nullable pour les besoins du test
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
}
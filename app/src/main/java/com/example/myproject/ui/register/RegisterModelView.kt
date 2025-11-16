package com.example.myproject.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myproject.data.model.RegisterRequest
import com.example.myproject.data.model.MessageResponse
import com.example.myproject.data.model.UserRole // NOUVEL IMPORT
import com.example.myproject.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Ce ViewModel gère la logique d'inscription
class RegisterViewModel : ViewModel() {

    // LiveData pour l'état d'inscription (messages d'erreur ou de succès)
    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> = _registrationStatus

    // LiveData pour stocker la réponse complète de succès
    private val _messageResponse = MutableLiveData<MessageResponse?>()
    val messageResponse: LiveData<MessageResponse?> = _messageResponse

    /**
     * Déclenche l'appel API d'inscription avec tous les champs requis et optionnels.
     * * @param email L'adresse e-mail de l'utilisateur (Obligatoire).
     * @param password Le mot de passe de l'utilisateur (Obligatoire).
     * @param role Le rôle de l'utilisateur (Obligatoire).
     * @param username Le nom d'utilisateur (Obligatoire).
     * @param location La localisation (Optionnel, peut être null).
     * @param phoneNumber Le numéro de téléphone (Optionnel, peut être null).
     * @param documentUrl L'URL du document (Optionnel, peut être null).
     */
    fun registerUser(
        email: String,
        password: String,
        role: UserRole, // Le type est UserRole, pas String
        username: String,
        location: String? = null, // Valeur par défaut null
        phoneNumber: String? = null, // Valeur par défaut null
        documentUrl: String? = null // Valeur par défaut null
    ) {
        // 1. Indiquer que l'inscription est en cours
        _registrationStatus.value = "Inscription en cours..."
        _messageResponse.value = null

        // 2. Création de l'objet requête avec TOUS les paramètres
        val request = RegisterRequest(
            email = email,
            password = password,
            role = role,
            username = username,
            location = location,
            phoneNumber = phoneNumber,
            documentUrl = documentUrl
        )

        // 3. Exécuter l'appel API de manière asynchrone
        RetrofitClient.api.register(request).enqueue(object : Callback<MessageResponse> {

            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Succès
                        _messageResponse.value = body
                        _registrationStatus.value = "Inscription réussie : ${body.message}"
                    } else {
                        _registrationStatus.value = "Erreur: Réponse du serveur vide après succès."
                    }
                } else {
                    // Échec de la requête (Ex: 4xx, 5xx)
                    val errorBody = response.errorBody()?.string() ?: "Erreur inconnue"
                    _registrationStatus.value = "Échec de l'inscription: ${response.code()} - $errorBody"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                // Échec réseau
                _registrationStatus.value = "Erreur réseau: ${t.localizedMessage ?: "Veuillez vérifier votre connexion."}"
            }
        })
    }
}
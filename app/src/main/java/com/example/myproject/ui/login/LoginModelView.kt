package com.example.myproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myproject.data.model.AuthRequest
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Ce ViewModel gère la logique d'authentification pour LoginActivity
class LoginViewModel : ViewModel() {

    // LiveData pour stocker et observer le statut de l'opération (pour l'UI)
    // Le type est une chaîne pour un message d'état simple, mais il peut être amélioré
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    // LiveData pour stocker la réponse de l'API en cas de succès
    private val _authResponse = MutableLiveData<AuthResponse?>()
    val authResponse: LiveData<AuthResponse?> = _authResponse

    /**
     * Déclenche l'appel API de connexion
     * @param email L'adresse e-mail de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    fun loginUser(email: String, password: String) {
        // 1. Indiquer que la connexion est en cours
        _loginStatus.value = "Connexion en cours..."
        _authResponse.value = null // Réinitialiser toute réponse précédente

        // Préparation de l'objet requête
        val request = AuthRequest(email, password)

        // 2. Exécuter l'appel API de manière asynchrone (non-bloquante)
        RetrofitClient.api.login(request).enqueue(object : Callback<AuthResponse> {

            // En cas de réponse du serveur (même si c'est une erreur 4xx/5xx)
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // 3. Succès : mettre à jour la LiveData de la réponse
                        _authResponse.value = body
                        _loginStatus.value = "Connexion réussie !"

                        // NOTE: Ici, vous devriez enregistrer le token (ex: dans SharedPreferences)
                        // ou naviguer vers l'écran principal.
                    } else {
                        _loginStatus.value = "Erreur: Réponse du serveur vide."
                    }
                } else {
                    // 4. Échec de la requête (Ex: 401 Unauthorized, 404 Not Found)
                    // Lire le message d'erreur du body d'erreur (si votre API le fournit)
                    val errorBody = response.errorBody()?.string() ?: "Erreur inconnue"
                    _loginStatus.value = "Échec de la connexion: ${response.code()} - $errorBody"
                }
            }

            // En cas d'échec de la requête (problèmes réseau, timeout, etc.)
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // 5. Échec réseau : informer l'utilisateur
                _loginStatus.value = "Erreur réseau: ${t.localizedMessage ?: "Vérifiez votre connexion."}"
            }
        })
    }
}
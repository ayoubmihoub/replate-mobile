package com.example.myproject.data.repository

import com.example.myproject.data.remote.ApiService
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.model.AuthRequest
import com.example.myproject.data.model.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginRepository(private val apiService: ApiService) {

    /**
     * Exécute l'appel API de connexion.
     */
    suspend fun loginUser(email: String, password: String): NetworkResult<AuthResponse> =
        withContext(Dispatchers.IO) {
            try {
                // Création du corps de la requête
                val request = AuthRequest(email, password)

                // Exécution synchrone de l'appel Retrofit (bloquant sur le thread IO)
                val response = apiService.login(request).execute()

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    return@withContext if (authResponse != null) {
                        NetworkResult.Success(authResponse)
                    } else {
                        // Réponse réussie mais corps vide, peu probable pour un login
                        NetworkResult.Error("Réponse du serveur inattendue.")
                    }
                } else {
                    // Gestion des erreurs HTTP (400, 401, 404, etc.)
                    val errorBody = response.errorBody()?.string() ?: "Erreur inconnue."

                    // Votre backend utilise probablement 401 pour InvalidCredentials
                    val errorMessage = if (response.code() == 401) {
                        "Identifiants invalides."
                    } else {
                        "Erreur ${response.code()}: $errorBody"
                    }
                    return@withContext NetworkResult.Error(errorMessage)
                }
            } catch (e: HttpException) {
                // Erreur HTTP plus générale (ex: 5xx du serveur)
                return@withContext NetworkResult.Error("Erreur serveur: ${e.message}")
            } catch (e: IOException) {
                // Erreur réseau (pas de connexion, timeout)
                return@withContext NetworkResult.Error("Erreur réseau. Veuillez vérifier votre connexion.")
            } catch (e: Exception) {
                // Toute autre exception inattendue
                return@withContext NetworkResult.Error("Une erreur inattendue s'est produite: ${e.message}")
            }
        }
}
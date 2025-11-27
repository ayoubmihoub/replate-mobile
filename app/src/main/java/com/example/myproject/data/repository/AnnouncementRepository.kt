package com.example.myproject.data.repository

import com.example.myproject.data.model.Announcement
import com.example.myproject.data.model.AnnouncementRequest
import com.example.myproject.data.remote.ApiService
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.session.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

// Classe interne pour décoder les messages d'erreur JSON standard de Spring Boot
private data class ErrorResponse(val message: String?, val error: String?)

class AnnouncementRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager // Contient le jeton
) { // Début de la classe

    // Helper pour extraire un message d'erreur lisible d'un corps de réponse
    private fun extractErrorMessage(errorBodyString: String?, defaultMessage: String): String {
        return if (errorBodyString != null) {
            try {
                // Tente de désérialiser le corps d'erreur en JSON (standard Spring Boot)
                val errorResponse = Gson().fromJson(errorBodyString, ErrorResponse::class.java)

                // Priorité au champ 'message', sinon utiliser 'error', sinon le corps brut
                errorResponse.message ?: errorResponse.error ?: errorBodyString
            } catch (e: Exception) {
                // Si la désérialisation échoue, retourne le corps brut
                defaultMessage + ": " + errorBodyString.take(50)
            }
        } else {
            defaultMessage
        }
    }

    // MÉTHODE D'AIDE pour récupérer le token JWT
    // C'est la présence de cette méthode DANS la classe qui résout l'erreur de référence.
    private fun getAuthToken(): String? {
        // Supposons que SessionManager a une méthode pour récupérer le jeton
        val token = sessionManager.getAuthToken()
        // Ajout du préfixe "Bearer " pour l'en-tête Authorization
        return token?.let { "Bearer $it" }
    }

    // -----------------------------------------------------------------------
    // CRÉATION D'ANNONCE
    // -----------------------------------------------------------------------
    suspend fun createAnnouncement(request: AnnouncementRequest): NetworkResult<Announcement> =
        withContext(Dispatchers.IO) {
            val authToken = getAuthToken() // Référence maintenant résolue

            if (authToken == null) {
                return@withContext NetworkResult.Error<Announcement>("Session utilisateur invalide ou jeton manquant. Veuillez vous reconnecter.")
            }

            return@withContext try {
                // Passage de l'authToken à l'ApiService pour l'en-tête Authorization
                val response = apiService.createAnnouncement(
                    request = request,
                    token = authToken
                ).execute()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error<Announcement>("Réponse du serveur réussie mais vide (204 No Content).", null)
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString, "Erreur de publication (Code ${response.code()})")
                    // Le code 401 (Unauthorized) sera traité ici
                    NetworkResult.Error<Announcement>(errorMessage, null)
                }
            } catch (e: IOException) {
                NetworkResult.Error<Announcement>("Erreur réseau. Vérifiez votre connexion.", null)
            } catch (e: Exception) {
                NetworkResult.Error<Announcement>("Erreur inattendue lors de l'envoi: ${e.message}", null)
            }
        }

    // -----------------------------------------------------------------------
    // RÉCUPÉRATION DES ANNONCES
    // -----------------------------------------------------------------------
    suspend fun getMyAnnouncements(): NetworkResult<List<Announcement>> =
        withContext(Dispatchers.IO) {
            val authToken = getAuthToken() // Référence maintenant résolue

            if (authToken == null) {
                return@withContext NetworkResult.Error<List<Announcement>>("Session utilisateur invalide. Veuillez vous reconnecter.")
            }

            return@withContext try {
                // Passage de l'authToken à l'ApiService pour l'en-tête Authorization
                val response = apiService.getMyAnnouncements(
                    token = authToken
                ).execute()

                if (response.isSuccessful) {
                    val body = response.body()
                    // Si le corps est null, renvoie une liste vide, ce qui est un succès
                    NetworkResult.Success(body ?: emptyList())
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString, "Erreur de chargement des annonces (Code ${response.code()})")
                    // Le code 401 sera traité ici
                    NetworkResult.Error<List<Announcement>>(errorMessage, null)
                }
            } catch (e: IOException) {
                NetworkResult.Error<List<Announcement>>("Erreur réseau. Vérifiez votre connexion.", null)
            } catch (e: Exception) {
                NetworkResult.Error<List<Announcement>>("Erreur inattendue lors du chargement: ${e.message}", null)
            }
        }
} // Fin de la classe
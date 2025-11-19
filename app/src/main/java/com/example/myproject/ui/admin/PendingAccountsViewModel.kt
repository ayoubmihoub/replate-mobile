package com.example.myproject.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.data.model.MessageResponse
import com.example.myproject.data.model.User
import com.example.myproject.data.remote.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingAccountsViewModel(private val token: String) : ViewModel() {

    // LiveData pour la liste des utilisateurs en attente
    private val _pendingUsers = MutableLiveData<List<User>>()
    val pendingUsers: LiveData<List<User>> = _pendingUsers

    // LiveData pour les messages de statut (succès, erreur, chargement)
    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    // LiveData pour l'état de chargement
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Récupère la liste des comptes en attente de validation depuis l'API.
     */
    fun fetchPendingAccounts() {
        _isLoading.value = true
        _statusMessage.value = "Chargement des comptes en attente..."

        RetrofitClient.api.getPendingAccounts(token).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    _pendingUsers.value = users
                    _statusMessage.value = if (users.isEmpty()) {
                        "Aucun compte en attente de validation."
                    } else {
                        "${users.size} compte(s) en attente de validation."
                    }
                } else {
                    // Gestion des erreurs HTTP courantes
                    when (response.code()) {
                        401 -> _statusMessage.value = "Erreur d'authentification. Token invalide ou expiré."
                        403 -> _statusMessage.value = "Accès refusé. Vous n'avez pas les permissions d'admin."
                        404 -> _statusMessage.value = "Endpoint non trouvé. Vérifiez l'URL de l'API."
                        else -> _statusMessage.value = "Erreur serveur: ${response.code()}"
                    }
                    _pendingUsers.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _statusMessage.value = "Erreur réseau: ${t.message}"
                _pendingUsers.value = emptyList()
            }
        })
    }

    /**
     * Envoie une requête à l'API pour valider un compte utilisateur spécifique.
     * @param userId L'identifiant de l'utilisateur à valider.
     */
    fun validateAccount(userId: Long) {
        _statusMessage.value = "Validation du compte en cours..."

        RetrofitClient.api.validateAccount(userId, token).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _statusMessage.value = "Compte validé avec succès!"
                    // Une fois validé, on recharge la liste pour le retirer de l'affichage
                    fetchPendingAccounts()
                } else {
                    // Gestion des erreurs
                    when (response.code()) {
                        401 -> _statusMessage.value = "Token expiré. Veuillez vous reconnecter."
                        404 -> _statusMessage.value = "Utilisateur non trouvé."
                        else -> _statusMessage.value = "Erreur lors de la validation: ${response.code()}"
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _statusMessage.value = "Erreur réseau: ${t.message}"
            }
        })
    }

    /**
     * Fonction à implémenter pour refuser un compte.
     */
    fun refuseAccount(userId: Long) {
        // Implémentez le refus ici (nécessite un appel API pour la suppression ou le marquage)
        _statusMessage.value = "Fonctionnalité de refus à implémenter"
    }
}
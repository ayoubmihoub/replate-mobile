package com.example.myproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.repository.LoginRepository
import com.example.myproject.data.remote.NetworkResult
import kotlinx.coroutines.launch

class LoginModelView(private val loginRepository: LoginRepository) : ViewModel() {

    // LiveData pour le statut du login
    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    // LiveData pour la réponse d'authentification
    private val _authResponse = MutableLiveData<AuthResponse?>()
    val authResponse: LiveData<AuthResponse?> = _authResponse

    // LiveData pour le chargement
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            when (val result = loginRepository.loginUser(email, password)) {
                is NetworkResult.Success -> {
                    val auth = result.data

                    if (auth != null) {
                        when (auth.role) {
                            // Cas 1 : Rôle ADMINISTRATEUR (Nécessite Validation)
                            UserRole.ADMIN -> {

                                    _authResponse.value = auth
                                    _loginStatus.value = "Connexion réussie!"
                            }

                            // Cas 2 : Autres Rôles (INDIVIDUAL, MERCHANT, ASSOCIATION)
                            // Ces rôles n'ont PAS besoin de la vérification isValidate (ou l'on suppose qu'elle est toujours True)
                            UserRole.INDIVIDUAL, UserRole.MERCHANT, UserRole.ASSOCIATION -> {
                                if (auth.isVerified) {
                                    // On peut toujours ajouter une vérification de sécurité sur isValidate,
                                    // mais si le serveur renvoie Success, on considère la connexion comme réussie.
                                    _authResponse.value = auth
                                    _loginStatus.value = "Connexion réussie!"
                                }else{
                                    _authResponse.value = null
                                    _loginStatus.value = "Compte n'est pas encore valide!"
                                }

                            }

                            // Si vous avez d'autres rôles
                            else -> {
                                // ... Gérer d'autres rôles si nécessaire
                                _authResponse.value = auth
                                _loginStatus.value = "Connexion réussie (Rôle spécial)!"
                            }
                        }

                    } else {
                        _authResponse.value = null
                        _loginStatus.value = "Erreur : réponse invalide du serveur."
                    }
                }
                is NetworkResult.Error -> {
                    _loginStatus.value = result.message ?: "Erreur inconnue"
                    _authResponse.value = null
                }
            }
            _isLoading.value = false
        }
    }


    // Méthode pour réinitialiser le statut
    fun resetLoginStatus() {
        _loginStatus.value = ""
        _authResponse.value = null
    }
}

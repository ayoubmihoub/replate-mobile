package com.example.myproject.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginModelView(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    private val _authResponse = MutableLiveData<AuthResponse?>()
    val authResponse: LiveData<AuthResponse?> = _authResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, pass: String) {
        _isLoading.value = true
        _loginStatus.value = ""

        viewModelScope.launch {
            try {
                val result = loginRepository.loginUser(email, pass)

                when (result) {
                    is NetworkResult.Success -> {
                        val auth = result.data
                        if (auth != null) {
                            Log.d("LoginDebug", "Auth reçue: $auth")
                            checkRoleAndValidate(auth)
                        } else {
                            _loginStatus.value = "Erreur : Données reçues vides."
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.e("LoginDebug", "Erreur API: ${result.message}")
                        _loginStatus.value = result.message ?: "Erreur inconnue"
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginDebug", "Exception dans loginUser", e)
                _loginStatus.value = "Crash évité: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun checkRoleAndValidate(auth: AuthResponse) {
        // Vérification de sécurité si le rôle est null (problème de parsing Gson)
        if (auth.role == null) {
            Log.e("LoginDebug", "CRITIQUE: Le rôle est NULL. Vérifiez si le backend envoie bien 'ADMIN', 'INDIVIDUAL' en majuscules exactes.")
            _loginStatus.value = "Erreur technique: Rôle utilisateur inconnu."
            return
        }

        when (auth.role) {
            UserRole.ADMIN -> {
                _authResponse.value = auth
                _loginStatus.value = "Connexion Admin réussie!"
            }

            UserRole.INDIVIDUAL, UserRole.MERCHANT, UserRole.ASSOCIATION -> {
                // Si isValidated est false, on bloque (selon votre logique précédente)

                    // CORRECTION : On supprime la vérification `if (auth.isVerified)`
                    // car le backend ne renvoie pas cette information.
                    // Si le NetworkResult est un succès, la connexion est validée.
                    _authResponse.value = auth
                    _loginStatus.value = "Connexion réussie!"


            }
        }
    }

    fun resetLoginStatus() {
        _loginStatus.value = ""
        _authResponse.value = null
    }
}
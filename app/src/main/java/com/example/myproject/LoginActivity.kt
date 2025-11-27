package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.remote.RetrofitClient
import com.example.myproject.data.repository.LoginRepository
import com.example.myproject.data.session.SessionManager // <-- NOUVEL IMPORT
import com.example.myproject.data.ui.login.ViewModelFactory
import com.example.myproject.ui.login.LoginModelView

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView
    private lateinit var loginViewModel: LoginModelView

    private fun decodeJwt(token: String): Boolean {
        return try {
            val parts = token.split(".")
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT))
            payload.contains("\"validated\":true")
        } catch (e: Exception) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        if (checkExistingTokenAndNavigate()) return

        initViews()
        initViewModel()
        observeViewModel()
        setupListeners()
    }

    private fun initViews() {
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        signupLink = findViewById(R.id.text_signup_link)
    }

    private fun initViewModel() {
        val loginRepository = LoginRepository(RetrofitClient.api)
        val factory = ViewModelFactory(loginRepository)
        loginViewModel = ViewModelProvider(this, factory)[LoginModelView::class.java]
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            // Nettoyer les SharedPreferences et le SessionManager avant de tenter une nouvelle connexion
            val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            SessionManager.clearInMemorySession() // <-- NETTOYAGE DANS SESSIONMANAGER

            if (validateLoginInputs()) {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString()
                loginViewModel.loginUser(email, password)
            } else {
                showToast("Veuillez vérifier votre email et mot de passe.")
            }
        }

        signupLink.setOnClickListener {
            navigateToSignup()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginStatus.observe(this) { status ->
            if (!status.isNullOrEmpty()) showToast(status)
        }

        loginViewModel.authResponse.observe(this) { authResponse ->
            authResponse?.let { handleSuccessfulLogin(it) }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            updateUIForLoading(isLoading)
        }
    }

    // ⭐ FONCTION MODIFIÉE : Sauvegarde dans SharedPreferences ET SessionManager
    private fun handleSuccessfulLogin(authResponse: AuthResponse) {

        val userRole = authResponse.role ?: run {
            showToast("Erreur technique : Rôle utilisateur inconnu.")
            loginViewModel.resetLoginStatus()
            return
        }

        val token = authResponse.token ?: ""
        val validatedFromJwt = decodeJwt(token)

        if (userRole == UserRole.ASSOCIATION || userRole == UserRole.MERCHANT) {
            if (!validatedFromJwt) {
                showToast("حسابك مازال يستنى موافقة من المسؤول.")
                loginViewModel.resetLoginStatus()
                return
            }
        }

        // 1. Sauvegarder le token dans SessionManager pour les requêtes immédiates
        SessionManager.saveAuthToken(token) // <-- L'APPEL MANQUANT POUR L'ANNONCEMENTREPOSITORY

        // 2. Sauvegarder dans SharedPreferences pour la persistance
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("TOKEN", token)
            putString("ROLE", userRole.name)
            putLong("USER_ID", authResponse.id)
            putString("USERNAME", authResponse.email ?: "")
            apply()
        }

        navigateBasedOnRole(userRole)
    }

    // ⭐ FONCTION MODIFIÉE : Charge le jeton existant dans le SessionManager au démarrage
    private fun checkExistingTokenAndNavigate(): Boolean {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        if (token.isNotEmpty()) {
            // Charger le jeton dans le SessionManager pour les requêtes futures
            SessionManager.saveAuthToken(token) // <-- NOUVEL APPEL POUR INITIALISER LA SESSION

            val roleName = sharedPref.getString("ROLE", UserRole.INDIVIDUAL.name)
                ?: UserRole.INDIVIDUAL.name
            return try {
                val role = UserRole.valueOf(roleName)
                navigateBasedOnRole(role)
                true
            } catch (e: IllegalArgumentException) {
                // Si le rôle est invalide, nettoyer et forcer la reconnexion
                sharedPref.edit().clear().apply()
                SessionManager.clearInMemorySession()
                false
            }
        }
        return false
    }

    private fun navigateBasedOnRole(role: UserRole) {
        when (role) {
            UserRole.ADMIN -> navigateToAdminDashboard()
            UserRole.INDIVIDUAL,
            UserRole.MERCHANT,
            UserRole.ASSOCIATION -> navigateToAllowLocation(role)
        }
    }

    private fun navigateToAllowLocation(role: UserRole) {
        val intent = Intent(this, AllowLocationActivity::class.java).apply {
            putExtra("TARGET_ROLE", role.name)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun updateUIForLoading(isLoading: Boolean) {
        // ... (non modifié)
    }

    private fun validateLoginInputs(): Boolean {
        // ... (non modifié)
        return true
    }

    private fun navigateToAdminDashboard() {
        // ... (non modifié)
    }

    private fun navigateToSignup() {
        // ... (non modifié)
    }

    private fun showToast(message: String) {
        // ... (non modifié)
    }

    override fun onBackPressed() {
        if (checkExistingTokenAndNavigate()) return
        super.onBackPressed()
    }
}
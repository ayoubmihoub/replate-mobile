package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.remote.RetrofitClient
import com.example.myproject.data.repository.LoginRepository
// LIGNE CORRIGÉE : L'import doit venir du package 'ui.login'
import com.example.myproject.data.ui.login.ViewModelFactory
import com.example.myproject.ui.login.LoginModelView

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView

    private lateinit var loginViewModel: LoginModelView

    // Tag pour le débogage dans Logcat
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // 1. Vérification automatique au démarrage
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
        val apiService = RetrofitClient.api
        val loginRepository = LoginRepository(apiService)
        // ViewModelFactory est maintenant correctement résolu ici
        val factory = ViewModelFactory(loginRepository)
        loginViewModel = ViewModelProvider(this, factory)[LoginModelView::class.java]
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            if (validateLoginInputs()) {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString()

                Log.d(TAG, "Clic Login: Tentative de connexion pour $email")
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
        // Gestion des messages de statut/erreur
        loginViewModel.loginStatus.observe(this) { status ->
            if (!status.isNullOrEmpty()) {
                showToast(status)
                Log.i(TAG, "Status Login: $status")
            }
        }

        // Gestion de la réussite de la connexion
        // C'EST ICI QUE LA NAVIGATION EST DÉCLENCHÉE
        loginViewModel.authResponse.observe(this) { authResponse ->
            authResponse?.let { handleSuccessfulLogin(it) }
        }

        // Gestion de l'état de chargement (désactive les boutons)
        loginViewModel.isLoading.observe(this) { isLoading ->
            updateUIForLoading(isLoading)
        }
    }

    private fun handleSuccessfulLogin(authResponse: com.example.myproject.data.model.AuthResponse) {
        Log.d(TAG, "Login réussi, traitement de la réponse...")

        // On vérifie que le rôle n'est pas null avant de l'utiliser
        val userRole = authResponse.role

        if (userRole == null) {
            Log.e(TAG, "ERREUR CRITIQUE: Le rôle est null dans la réponse API.")
            showToast("Erreur technique : Rôle utilisateur inconnu.")
            // On reset pour permettre une nouvelle tentative
            loginViewModel.resetLoginStatus()
            return
        }

        // Sauvegarde dans les préférences
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("TOKEN", authResponse.token)
            putString("ROLE", userRole.name) // Utilisation sécurisée de userRole (non-null)
            putLong("USER_ID", authResponse.id)
            putString("USERNAME", authResponse.email)
            apply()
        }

        Log.d(TAG, "Token sauvegardé. Navigation vers l'écran du rôle: ${userRole.name}")
        navigateBasedOnRole(userRole)
    }

    private fun checkExistingTokenAndNavigate(): Boolean {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        if (token.isNotEmpty()) {
            val roleName = sharedPref.getString("ROLE", UserRole.INDIVIDUAL.name) ?: UserRole.INDIVIDUAL.name
            try {
                val role = UserRole.valueOf(roleName)
                Log.d(TAG, "Token existant trouvé. Redirection auto vers $role")
                navigateBasedOnRole(role)
                return true
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Rôle stocké invalide ($roleName). Nettoyage des préférences.")
                sharedPref.edit().clear().apply()
            }
        }
        return false
    }

    private fun navigateBasedOnRole(role: UserRole) {
        when (role) {
            UserRole.ADMIN -> navigateToAdminDashboard()
            UserRole.INDIVIDUAL -> navigateToHome2()
            else -> navigateToHomePage()
        }
    }

    private fun updateUIForLoading(isLoading: Boolean) {
        loginButton.isEnabled = !isLoading
        loginButton.text = if (isLoading) getString(R.string.connecting) else getString(R.string.login)
        inputEmail.isEnabled = !isLoading
        inputPassword.isEnabled = !isLoading
    }

    private fun validateLoginInputs(): Boolean {
        var isValid = true
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()

        if (email.isEmpty()) {
            inputEmail.error = "L'email est requis."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.error = "Format d'email invalide."
            isValid = false
        } else inputEmail.error = null

        if (password.isEmpty()) {
            inputPassword.error = "Le mot de passe est requis."
            isValid = false
        } else if (password.length < 6) {
            inputPassword.error = "Le mot de passe doit contenir au moins 6 caractères."
            isValid = false
        } else inputPassword.error = null

        return isValid
    }

    // --- Fonctions de Navigation ---

    private fun navigateToHome2() {
        startActivity(Intent(this, Home2Activity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun navigateToHomePage() {
        startActivity(Intent(this, HomePage::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun navigateToAdminDashboard() {
        startActivity(Intent(this, AdminDashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun navigateToSignup() {
        startActivity(Intent(this, Activity4::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (checkExistingTokenAndNavigate()) return
        super.onBackPressed()
    }
}
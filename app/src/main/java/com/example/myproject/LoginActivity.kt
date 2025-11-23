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
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.remote.RetrofitClient
import com.example.myproject.data.repository.LoginRepository
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
            val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

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

    private fun handleSuccessfulLogin(authResponse: com.example.myproject.data.model.AuthResponse) {

        val userRole = authResponse.role ?: run {
            showToast("Erreur technique : Rôle utilisateur inconnu.")
            loginViewModel.resetLoginStatus()
            return
        }

        val validatedFromJwt = decodeJwt(authResponse.token ?: "")

        if (userRole == UserRole.ASSOCIATION || userRole == UserRole.MERCHANT) {
            if (!validatedFromJwt) {
                showToast("حسابك مازال يستنى موافقة من المسؤول.")
                loginViewModel.resetLoginStatus()
                return
            }
        }

        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("TOKEN", authResponse.token ?: "")
            putString("ROLE", userRole.name)
            putLong("USER_ID", authResponse.id)
            putString("USERNAME", authResponse.email ?: "")
            apply()
        }

        navigateBasedOnRole(userRole)
    }

    private fun checkExistingTokenAndNavigate(): Boolean {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""

        if (token.isNotEmpty()) {
            val roleName = sharedPref.getString("ROLE", UserRole.INDIVIDUAL.name)
                ?: UserRole.INDIVIDUAL.name
            return try {
                val role = UserRole.valueOf(roleName)
                navigateBasedOnRole(role)
                true
            } catch (e: IllegalArgumentException) {
                sharedPref.edit().clear().apply()
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

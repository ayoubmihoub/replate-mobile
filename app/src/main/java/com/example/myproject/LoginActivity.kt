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
import com.example.myproject.data.remote.ViewModelFactory
import com.example.myproject.ui.login.LoginModelView

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView

    private lateinit var loginViewModel: LoginModelView

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
        val apiService = RetrofitClient.api
        val loginRepository = LoginRepository(apiService)
        val factory = ViewModelFactory(loginRepository)
        loginViewModel = ViewModelProvider(this, factory)[LoginModelView::class.java]
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
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
            if (status.isNotEmpty()) showToast(status)
        }

        loginViewModel.authResponse.observe(this) { authResponse ->
            authResponse?.let { handleSuccessfulLogin(it) }
        }

        loginViewModel.isLoading.observe(this) { updateUIForLoading(it) }
    }

    private fun handleSuccessfulLogin(authResponse: com.example.myproject.data.model.AuthResponse) {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("TOKEN", authResponse.token)
            putString("ROLE", authResponse.role.name)
            putLong("USER_ID", authResponse.id)
            putString("USERNAME", authResponse.email)
            apply()
        }

        when (authResponse.role) {
            UserRole.ADMIN -> navigateToAdminDashboard()
            else -> navigateToMainFlow()
        }
    }

    private fun checkExistingTokenAndNavigate(): Boolean {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("TOKEN", "") ?: ""
        if (token.isNotEmpty()) {
            val roleName = sharedPref.getString("ROLE", UserRole.INDIVIDUAL.name) ?: UserRole.INDIVIDUAL.name
            try {
                val role = UserRole.valueOf(roleName)
                if (role == UserRole.ADMIN) navigateToAdminDashboard() else navigateToMainFlow()
                return true
            } catch (e: IllegalArgumentException) {
                sharedPref.edit().remove("TOKEN").remove("ROLE").apply()
            }
        }
        return false
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

    private fun navigateToMainFlow() {
        startActivity(Intent(this, AllowLocationActivity::class.java).apply {
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

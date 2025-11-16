package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class SignupMerchant3Activity : AppCompatActivity() {

    private lateinit var incomingIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_merchant3)

        // Récupérer les données de l'activité précédente
        incomingIntent = intent

        val createAccountButton: Button = findViewById(R.id.btn_create_account)
        val previousButton: Button = findViewById(R.id.btn_previous)

        // --- Logique du bouton "Create Account" ---
        createAccountButton.setOnClickListener {
            if (isFormValid()) {
                // Si la validation réussit, naviguer vers l'activité finale
                navigateToFinSignupActivity()
            } else {
                Toast.makeText(this, "Veuillez vérifier les erreurs et accepter les conditions.", Toast.LENGTH_LONG).show()
            }
        }

        // --- Logique du bouton "Previous" ---
        previousButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Effectue une validation complète des mots de passe et des termes.
     */
    private fun isFormValid(): Boolean {
        var isValid = true

        val passwordInput = findViewById<EditText>(R.id.input_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.input_confirm_password)
        val termsCheckbox = findViewById<CheckBox>(R.id.checkbox_terms)

        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        val MIN_PASSWORD_LENGTH = 6

        // 1. Validation du mot de passe
        if (password.isEmpty()) {
            passwordInput.error = "Le mot de passe est requis."
            isValid = false
        } else if (password.length < MIN_PASSWORD_LENGTH) {
            passwordInput.error = "Doit contenir au moins $MIN_PASSWORD_LENGTH caractères."
            isValid = false
        } else {
            passwordInput.error = null
        }

        // 2. Validation de la confirmation du mot de passe
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.error = "La confirmation est requise."
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordInput.error = "Les mots de passe ne correspondent pas."
            isValid = false
        } else {
            confirmPasswordInput.error = null
        }

        // 3. Validation de l'accord sur les termes
        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Vous devez accepter les conditions d'utilisation.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    /**
     * Lance l'activité de fin d'inscription, en passant toutes les données accumulées.
     */
    private fun navigateToFinSignupActivity() {
        val password = findViewById<EditText>(R.id.input_password).text.toString()

        val intent = Intent(this, FinSignupMerchantActivity::class.java).apply {
            // Données de l'activité 1 (Nom, Email, Contact)
            putExtra("fullName", incomingIntent.getStringExtra("fullName"))
            putExtra("email", incomingIntent.getStringExtra("email"))
            putExtra("contact", incomingIntent.getStringExtra("contact"))

            // Données de l'activité 2 (URIs)
            putExtra("documentUri", incomingIntent.getStringExtra("documentUri"))
            putExtra("profilePictureUri", incomingIntent.getStringExtra("profilePictureUri"))

            // Nouvelle donnée de l'activité 3 (Mot de passe)
            putExtra("password", password)
        }
        startActivity(intent)
    }
}
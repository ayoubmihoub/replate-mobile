package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Patterns

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // 1. Initialisation des composants
        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        signupLink = findViewById(R.id.text_signup_link)

        // --- 1. GESTION DU CLIC "LOGIN" (avec contrôle de saisie) ---
        loginButton.setOnClickListener {
            if (validateLoginInputs()) {
                // Contrôle de saisie OK. On navigue.

                // Naviguer vers l'écran d'autorisation de localisation
                val intent = Intent(this, AllowLocationActivity::class.java)

                // EFFACE l'historique d'activités pour que l'utilisateur ne puisse pas
                // revenir au formulaire d'inscription ou de connexion avec le bouton Retour.
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)

            } else {
                Toast.makeText(this, "Veuillez vérifier votre email et mot de passe.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- 2. GESTION DU CLIC "SIGNUP" (Navigue vers Activity4) ---
        signupLink.setOnClickListener {
            // L'utilisateur est redirigé vers le milieu du processus d'inscription
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }
    }

    /**
     * Fonction de validation des champs de l'écran de connexion.
     * La même logique que précédemment.
     */
    private fun validateLoginInputs(): Boolean {
        var isValid = true

        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()

        // 1. Validation de l'Email
        if (email.isEmpty()) {
            inputEmail.error = "L'email est requis."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.error = "Format d'email invalide."
            isValid = false
        } else {
            inputEmail.error = null
        }

        // 2. Validation du Mot de Passe
        if (password.isEmpty()) {
            inputPassword.error = "Le mot de passe est requis."
            isValid = false
        } else if (password.length < 6) {
            inputPassword.error = "Mot de passe invalide."
            isValid = false
        } else {
            inputPassword.error = null
        }

        return isValid
    }
}
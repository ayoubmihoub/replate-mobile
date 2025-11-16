package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class SignupAssociation3Activity : AppCompatActivity() {

    // Constante pour la longueur minimale du mot de passe
    private val MIN_PASSWORD_LENGTH = 6

    // Déclaration des champs de saisie pour l'accès dans isFormValid()
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var termsCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association3)

        // 1. Trouver les vues
        passwordInput = findViewById(R.id.input_password)
        confirmPasswordInput = findViewById(R.id.input_confirm_password)
        termsCheckbox = findViewById(R.id.checkbox_terms)
        val previousButton: Button = findViewById(R.id.btn_previous)
        val createAccountButton: Button = findViewById(R.id.btn_create_account)

        // 2. Implémenter le clic sur "Previous"
        previousButton.setOnClickListener {
            finish()
        }

        // 3. Implémenter la logique du bouton final (Create Account)
        createAccountButton.setOnClickListener {
            onCreateAccountClicked()
        }
    }

    /**
     * Gère l'événement de clic sur le bouton "Create Account" et effectue la validation.
     */
    private fun onCreateAccountClicked() {
        if (isFormValid()) {
            // Si la validation réussit, naviguer vers l'activité finale
            navigateToFinSignupActivity()
        } else {
            Toast.makeText(this, "Veuillez vérifier les erreurs de mot de passe et accepter les conditions.", Toast.LENGTH_LONG).show()
        }
    }

    // --- LOGIQUE DE VALIDATION DU MOT DE PASSE ---
    /**
     * Effectue une validation complète des mots de passe et des termes.
     */
    private fun isFormValid(): Boolean {
        var isValid = true

        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

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
            // On ne met pas d'erreur sur la CheckBox mais on utilise le Toast global
            isValid = false
        }

        return isValid
    }
    // ------------------------------------------------

    /**
     * Lance l'activité de fin d'inscription (FinSignupActivity).
     */
    private fun navigateToFinSignupActivity() {
        val intent = Intent(this, FinSignupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
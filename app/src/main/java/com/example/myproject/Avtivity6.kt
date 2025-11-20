package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.UserRole
import com.example.myproject.ui.register.RegisterViewModel

/**
 * Activité finale du flux d'inscription.
 * Elle collecte le mot de passe et déclenche l'appel API via le ViewModel.
 */
class Activity6 : AppCompatActivity() {

    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText

    // Déclaration du ViewModel
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)

        // 1. Initialisation du ViewModel
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // --- Récupération des données passées via Intent ---
        val incomingIntent = intent
        val fullName = incomingIntent.getStringExtra("fullName")
        val email = incomingIntent.getStringExtra("email")
        val contact = incomingIntent.getStringExtra("contact")
        val location = incomingIntent.getStringExtra("location")

        // VÉRIFICATION CRITIQUE : Les données essentielles doivent être présentes.
        if (fullName.isNullOrBlank() || email.isNullOrBlank() || contact.isNullOrBlank()) {
            Toast.makeText(this, "Erreur de données. Redirection vers le début de l'inscription.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // --- Initialisation des Vues ---
        inputPassword = findViewById(R.id.input_password)
        inputConfirmPassword = findViewById(R.id.input_confirm_password)
        val completeButton: Button = findViewById(R.id.btn_create_account)
        val prevButton: Button = findViewById(R.id.btn_previous)
        val loginLink: TextView = findViewById(R.id.text_login_link)

        // NOUVEAU: Bouton Merchant pour changer de flux d'inscription
        val merchantButton: Button = findViewById(R.id.btn_merchant)


        // --- LOGIQUE D'INSCRIPTION MERCHANT ---
        merchantButton.setOnClickListener {
            val intent = Intent(this, SignupMerchant1Activity::class.java)
            startActivity(intent)
            // Fermer toutes les activités précédentes dans la pile pour commencer le nouveau flux proprement
            finishAffinity()
        }

        // --- LOGIQUE EXISTANTE ---

        // Clic sur le lien de connexion
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }


        // 2. Observation du statut d'inscription

        registerViewModel.registrationStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
        }

        registerViewModel.messageResponse.observe(this) { response ->
            if (response != null) {
                Toast.makeText(this, "Compte créé! Vous pouvez maintenant vous connecter.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }


        // 3. Logique du bouton Finaliser (pour INDIVIDUAL)
        completeButton.setOnClickListener {

            if (validateInputs()) {

                val password = inputPassword.text.toString().trim()
                val username = fullName
                val phoneNumber = contact
                val documentUrl: String? = null

                registerViewModel.registerUser(
                    email = email,
                    password = password,
                    role = UserRole.INDIVIDUAL,
                    username = username,
                    location = location,
                    phoneNumber = phoneNumber,
                    documentUrl = documentUrl
                )
            }
        }

        // Logique du bouton Précédent
        prevButton.setOnClickListener {
            finish() // Retourne à Activity5
        }
    }

    /**
     * Valide les mots de passe.
     */
    private fun validateInputs(): Boolean {
        val password = inputPassword.text.toString().trim()
        val confirmPassword = inputConfirmPassword.text.toString().trim()

        var isValid = true

        if (password.isEmpty()) {
            inputPassword.error = "Veuillez entrer le mot de passe."
            isValid = false
        } else if (password.length < 6) {
            inputPassword.error = "Le mot de passe doit avoir au moins 6 caractères."
            isValid = false
        } else {
            inputPassword.error = null
        }

        if (confirmPassword.isEmpty()) {
            inputConfirmPassword.error = "Veuillez confirmer le mot de passe."
            isValid = false
        } else if (password != confirmPassword) {
            inputConfirmPassword.error = "Les mots de passe ne correspondent pas."
            isValid = false
        } else {
            inputConfirmPassword.error = null
        }

        return isValid
    }
}
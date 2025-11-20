package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.widget.CheckBox
import android.widget.TextView // <-- Import nécessaire
import androidx.appcompat.app.AppCompatActivity

class SignupAssociation3Activity : AppCompatActivity() {

    // Constante pour la longueur minimale du mot de passe
    private val MIN_PASSWORD_LENGTH = 6

    // Déclaration des champs de saisie pour l'accès dans isFormValid()
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var termsCheckbox: CheckBox
    private lateinit var incomingIntent: Intent

    // NOUVELLES DÉCLARATIONS POUR LA NAVIGATION CROISÉE
    private lateinit var btnIndividual: Button
    private lateinit var btnMerchant: Button
    private lateinit var loginLink: TextView // <-- NOUVELLE DÉCLARATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association3)

        // Récupérer l'Intent contenant toutes les données précédentes
        incomingIntent = intent

        // 1. Trouver les vues standard
        passwordInput = findViewById(R.id.input_password)
        confirmPasswordInput = findViewById(R.id.input_confirm_password)
        termsCheckbox = findViewById(R.id.checkbox_terms)
        val previousButton: Button = findViewById(R.id.btn_previous)
        val createAccountButton: Button = findViewById(R.id.btn_create_account)

        // Initialisation des boutons de navigation croisée
        btnIndividual = findViewById(R.id.btn_individual)
        btnMerchant = findViewById(R.id.btn_merchant)
        loginLink = findViewById(R.id.text_login) // <-- INITIALISATION DU LIEN DE CONNEXION

        // --- GESTION DE LA NAVIGATION CROISÉE ---

        // Clic sur 'Individual' -> Activity4
        btnIndividual.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
            finishAffinity()
        }

        // Clic sur 'Merchant' -> SignupMerchant1Activity
        btnMerchant.setOnClickListener {
            val intent = Intent(this, SignupMerchant1Activity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        // Clic sur le lien 'Login' -> LoginActivity
        loginLink.setOnClickListener { // <-- NOUVEL ÉCOUTEUR DE CLIC
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux actuel
        }
        // ------------------------------------


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
            // Si la validation réussit, naviguer vers l'activité finale en passant TOUTES les données
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
     * Lance l'activité de fin d'inscription (FinSignupAssociationActivity) et passe toutes les données.
     * Note: On change la destination pour FinSignupAssociationActivity.
     */
    private fun navigateToFinSignupActivity() {
        val password = passwordInput.text.toString().trim()

        // Récupération sécurisée des données précédentes avec fallback si null
        val fullName = incomingIntent.getStringExtra("fullName") ?: ""
        val email = incomingIntent.getStringExtra("email") ?: ""
        val contact = incomingIntent.getStringExtra("contact") ?: ""
        val location = incomingIntent.getStringExtra("location") ?: ""
        val profilePictureUri = incomingIntent.getStringExtra("profilePictureUri") ?: ""
        val documentUri = incomingIntent.getStringExtra("documentUri") ?: "mock_document.pdf" // fallback mock

        // Vérification minimale pour éviter que FinSignupAssociationActivity ferme l'app
        if (fullName.isBlank() || email.isBlank() || contact.isBlank() || password.isBlank() || location.isBlank() || documentUri.isBlank()) {
            Toast.makeText(this, "Certaines données sont manquantes, veuillez compléter le formulaire.", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, FinSignupAssociationActivity::class.java).apply {
            putExtra("fullName", fullName)
            putExtra("email", email)
            putExtra("contact", contact)
            putExtra("location", location)
            putExtra("profilePictureUri", profilePictureUri)
            putExtra("documentUri", documentUri)
            putExtra("password", password)
        }

        startActivity(intent)
    }

}
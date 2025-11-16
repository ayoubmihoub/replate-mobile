package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider // Import pour ViewModelProvider
import com.example.myproject.data.model.UserRole // Import de l'énumération UserRole
import com.example.myproject.ui.register.RegisterViewModel // Import de votre ViewModel

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

        // --- Récupération des données passées via Intent (Données d'Activity4 et Activity5) ---
        val incomingIntent = intent
        val fullName = incomingIntent.getStringExtra("fullName")
        val email = incomingIntent.getStringExtra("email")
        val contact = incomingIntent.getStringExtra("contact") // Utilisé comme phoneNumber
        val location = incomingIntent.getStringExtra("location")
        // Note: profileImageUri est ignoré pour l'appel API RegisterRequest car il n'est pas dans le modèle (seul documentUrl est présent et optionnel).

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

        // 2. Observation du statut d'inscription

        // Affiche les messages d'état (Erreur réseau, En cours, Erreur serveur)
        registerViewModel.registrationStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
        }

        // Gère la réponse de succès de l'API
        registerViewModel.messageResponse.observe(this) { response ->
            if (response != null) {
                // L'inscription a réussi. Naviguer vers l'écran de connexion/accueil.
                Toast.makeText(this, "Compte créé! Vous pouvez maintenant vous connecter.", Toast.LENGTH_LONG).show()
                // Exemple de navigation:
                // val intent = Intent(this, LoginActivity::class.java)
                // startActivity(intent)
                // finishAffinity() // Ferme toutes les activités du flux d'inscription
            }
        }


        // --- 3. Logique du bouton Finaliser ---
        completeButton.setOnClickListener {

            // Validation côté client (mots de passe)
            if (validateInputs()) {

                val password = inputPassword.text.toString().trim()
                // Mappage des champs de l'UI aux champs de l'API RegisterRequest
                val username = fullName // full name utilisé comme username
                val phoneNumber = contact // contact utilisé comme phoneNumber

                // documentUrl est null car c'est un utilisateur individuel
                val documentUrl: String? = null

                // Déclenche l'appel API via le ViewModel
                registerViewModel.registerUser(
                    email = email,
                    password = password,
                    role = UserRole.INDIVIDUAL, // ⬅️ Utilisation de l'énumération comme confirmé
                    username = username,
                    location = location,
                    phoneNumber = phoneNumber,
                    documentUrl = documentUrl // Passé comme null car il est optionnel et non utilisé ici
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
package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.UserRole
import com.example.myproject.ui.register.RegisterViewModel

/**
 * Activité finale pour l'inscription d'une Association.
 * Elle rassemble toutes les données collectées et déclenche l'appel API via le ViewModel avec UserRole.ASSOCIATION.
 */
class FinSignupAssociationActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4) // Réutilisation du layout précédent

        // 1. Initialisation du ViewModel
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // --- Récupération de TOUTES les données passées via Intent ---
        val incomingIntent = intent
        val fullName = incomingIntent.getStringExtra("fullName")
        val email = incomingIntent.getStringExtra("email")
        val contact = incomingIntent.getStringExtra("contact") // phoneNumber
        val password = incomingIntent.getStringExtra("password")
        val location = incomingIntent.getStringExtra("location")
        val documentUri = incomingIntent.getStringExtra("documentUri")
        // val profilePictureUri = incomingIntent.getStringExtra("profilePictureUri") // Non utilisé dans l'API RegisterRequest

        // VÉRIFICATION CRITIQUE : Les données essentielles doivent être présentes.
        if (fullName.isNullOrBlank() || email.isNullOrBlank() || contact.isNullOrBlank() || password.isNullOrBlank() || location.isNullOrBlank() || documentUri.isNullOrBlank()) {
            Toast.makeText(this, "Erreur de données requises pour l'Association. Redirection.", Toast.LENGTH_LONG).show()
            finishAffinity()
            return
        }

        // Simuler une URL publique pour le document, car l'API attend une URL et non une URI locale.
        val documentUrl = "http://mockapi.com/documents/association/$fullName-${System.currentTimeMillis()}.pdf"


        // 2. Observation du statut d'inscription
        registerViewModel.registrationStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
        }

        // Gère la réponse de succès de l'API
        registerViewModel.messageResponse.observe(this) { response ->
            if (response != null) {
                Toast.makeText(this, "Compte Association créé! En attente de validation Admin.", Toast.LENGTH_LONG).show()

                // TODO: Naviguer vers l'écran de connexion/accueil
                // val intent = Intent(this, LoginActivity::class.java)
                // startActivity(intent)
                //finishAffinity() // Ferme toutes les activités du flux d'inscription
            }
        }

        // 3. Déclenche l'appel API immédiatement
        registerAssociation(
            email = email,
            password = password,
            username = fullName,
            location = location,
            phoneNumber = contact,
            documentUrl = documentUrl
        )
    }

    /**
     * Déclenche l'appel API d'inscription avec le rôle ASSOCIATION.
     */
    private fun registerAssociation(
        email: String,
        password: String,
        username: String,
        location: String,
        phoneNumber: String,
        documentUrl: String?
    ) {
        registerViewModel.registerUser(
            email = email,
            password = password,
            role = UserRole.ASSOCIATION, // ⬅️ Rôle de l'Association
            username = username,
            location = location, // ⬅️ Adresse de l'association
            phoneNumber = phoneNumber,
            documentUrl = documentUrl // ⬅️ URL du document requis
        )
    }
}
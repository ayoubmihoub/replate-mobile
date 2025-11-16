package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Patterns

// NOTE: Le nom de votre activité est Activity4, qui est utilisé ici.

class Activity4 : AppCompatActivity() {

    // Déclaration des champs de saisie et des boutons
    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText
    private lateinit var loginLink: TextView

    // NOUVELLE DÉCLARATION
    private lateinit var associationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4) // Assurez-vous que c'est bien le layout activity_4.xml qui est chargé

        // Initialisation des champs
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact)
        loginLink = findViewById(R.id.text_login_link)

        val nextButton: Button = findViewById(R.id.btn_next_form)
        val prevButton: Button = findViewById(R.id.btn_previous)

        // 1. Initialisation du bouton "Association"
        associationButton = findViewById(R.id.btn_association)
        val btnMerchant: Button = findViewById(R.id.btn_merchant)

        // --- GESTION DU CLIC "ASSOCIATION" ---
        associationButton.setOnClickListener {
            // Créer un Intent pour démarrer SignupAssociation1Activity
            // 🚨 Assurez-vous que cette classe existe et est déclarée dans le Manifeste
            val intent = Intent(this, SignupAssociation1Activity::class.java)
            startActivity(intent)
            // Optionnel : Fermer Activity4 pour ne pas revenir en arrière
            // finish()
        }
        btnMerchant.setOnClickListener {
            // Créer un Intent pour démarrer SignupAssociation1Activity
            // 🚨 Assurez-vous que cette classe existe et est déclarée dans le Manifeste
            val intent = Intent(this, SignupMerchant1Activity::class.java)
            startActivity(intent)
            // Optionnel : Fermer Activity4 pour ne pas revenir en arrière
            // finish()
        }


        // --- NOTE IMPORTANTE : GÉRER LE BOUTON "INDIVIDUAL" ---
        // Dans votre XML, le bouton "Individual" est actuellement sélectionné (orange).
        // Vous devriez ajouter une logique ici pour le bouton "Individual" (btn_individual)
        // pour rester sur cette page ou naviguer vers une page d'inscription individuelle
        // si nécessaire.

        // --- GESTION DU CLIC "NEXT" AVEC VALIDATION ---
        nextButton.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, Activity5::class.java).apply {
                    putExtra("fullName", inputFullName.text.toString())
                    putExtra("email", inputEmail.text.toString())
                    putExtra("contact", inputContact.text.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs de saisie.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- GESTION DU CLIC "PREVIOUS" ---
        prevButton.setOnClickListener {
            finish() // Retourne à l'activité précédente
        }

        // --- GESTION DU CLIC "LOGIN" ---
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    // ... (La fonction validateInputs() reste inchangée)
    private fun validateInputs(): Boolean {
        // ... (votre logique de validation)
        var isValid = true

        val fullName = inputFullName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val contact = inputContact.text.toString().trim()

        if (fullName.isEmpty()) {
            inputFullName.error = "Le nom complet est requis."
            isValid = false
        } else {
            inputFullName.error = null
        }

        if (email.isEmpty()) {
            inputEmail.error = "L'email est requis."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.error = "Format d'email invalide."
            isValid = false
        } else {
            inputEmail.error = null
        }

        if (contact.isEmpty()) {
            inputContact.error = "Le numéro de contact est requis."
            isValid = false
        } else if (contact.length < 8) {
            inputContact.error = "Le numéro doit contenir au moins 8 chiffres."
            isValid = false
        } else {
            inputContact.error = null
        }

        return isValid
    }
}
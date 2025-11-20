package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignupAssociation1Activity : AppCompatActivity() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText
    private lateinit var loginLink: TextView

    // NOUVELLES DÉCLARATIONS POUR LA NAVIGATION CROISÉE
    private lateinit var btnIndividual: Button
    private lateinit var btnMerchant: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association1)

        // 1. Initialisation des vues standard
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact)
        val nextButton: Button = findViewById(R.id.btn_next)
        val previousButton: Button = findViewById(R.id.btn_previous)
        loginLink = findViewById(R.id.text_login)

        // Initialisation des boutons de navigation croisée
        // 🚨 Assurez-vous que les IDs btn_individual et btn_merchant existent dans signup_association1.xml
        btnIndividual = findViewById(R.id.btn_individual)
        btnMerchant = findViewById(R.id.btn_merchant)

        // --- LOGIQUE DE NAVIGATION CROISÉE ---

        // Clic sur 'Individual' -> Activity4
        btnIndividual.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux actuel
        }

        // Clic sur 'Merchant' -> SignupMerchant1Activity
        btnMerchant.setOnClickListener {
            val intent = Intent(this, SignupMerchant1Activity::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux actuel
        }
        // ------------------------------------

        // 2. Définir l'action au clic du bouton "Next"
        nextButton.setOnClickListener {
            // Appel de la validation des champs
            if (validateInputs()) {
                navigateToNextStep()
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs de saisie (Nom et Email requis).", Toast.LENGTH_SHORT).show()
            }
        }
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // --- Gérer le bouton Previous ---
        previousButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Valide les champs de saisie (Nom complet et Email).
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        val fullName = inputFullName.text.toString().trim()
        val email = inputEmail.text.toString().trim()

        // 1. Validation du Nom Complet
        if (fullName.isEmpty()) {
            inputFullName.error = "Le nom complet de l'association est requis."
            isValid = false
        } else {
            inputFullName.error = null
        }

        // 2. Validation de l'Email
        if (email.isEmpty()) {
            inputEmail.error = "L'email est requis."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.error = "Format d'email invalide."
            isValid = false
        } else {
            inputEmail.error = null
        }

        return isValid
    }

    /**
     * Lance l'activité pour la deuxième étape de l'inscription et passe les données.
     */
    private fun navigateToNextStep() {
        val fullName = inputFullName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val contact = inputContact.text.toString().trim()

        val intent = Intent(this, SignupAssociation2Activity::class.java).apply {
            putExtra("fullName", fullName)
            putExtra("email", email)
            putExtra("contact", contact)
        }
        startActivity(intent)
    }
}
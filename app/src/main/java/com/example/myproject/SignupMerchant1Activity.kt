package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns // Import nécessaire pour la validation d'email

class SignupMerchant1Activity : AppCompatActivity() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText // Ajouté pour référence complète, même si non requis dans la demande
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association1) // J'utilise le layout du XML fourni

        // 1. Initialisation des vues
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact) // S'assurer que l'ID existe
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_previous)

        // 2. --- Logique pour le bouton "Next" avec validation ---
        nextButton.setOnClickListener {
            if (validateInputs()) {
                // Créer un Intent pour passer à la nouvelle activité (Merchant 2)
                val intent = Intent(this, SignupMerchant2Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs de saisie.", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. --- Logique optionnelle pour le bouton "Previous" ---
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
        // Note: La validation du contact pourrait être ajoutée ici si nécessaire.

        // 1. Validation du Nom Complet
        if (fullName.isEmpty()) {
            inputFullName.error = "Le nom complet est requis."
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

        // Si tous les checks sont passés, isValid reste true
        return isValid
    }
}
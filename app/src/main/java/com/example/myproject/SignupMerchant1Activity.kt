package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns

class SignupMerchant1Activity : AppCompatActivity() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // NOTE: Le nom du layout est assumé être signup_merchant1
        setContentView(R.layout.signup_merchant1)

        // 1. Initialisation des vues
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact)
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_previous)

        // 2. --- Logique pour le bouton "Next" avec validation ---
        nextButton.setOnClickListener {
            if (validateInputs()) {
                val fullName = inputFullName.text.toString().trim()
                val email = inputEmail.text.toString().trim()
                val contact = inputContact.text.toString().trim()

                // Passage des données à l'activité 2
                val intent = Intent(this, SignupMerchant2Activity::class.java).apply {
                    putExtra("fullName", fullName)
                    putExtra("email", email)
                    putExtra("contact", contact)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs de saisie.", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. --- Logique pour le bouton "Previous" ---
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
        // Le contact est ici laissé optionnel pour la simplicité, mais la validation pourrait être ajoutée.

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

        return isValid
    }
}
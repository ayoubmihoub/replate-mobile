package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns // Import nécessaire pour la validation d'email
import androidx.appcompat.app.AppCompatActivity

class SignupAssociation1Activity : AppCompatActivity() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association1)

        // 1. Initialisation des vues
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact) // Référence au champ de contact
        val nextButton: Button = findViewById(R.id.btn_next)
        val previousButton: Button = findViewById(R.id.btn_previous)

        // 2. Définir l'action au clic du bouton "Next"
        nextButton.setOnClickListener {
            // Appel de la validation des champs
            if (validateInputs()) {
                navigateToNextStep()
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs de saisie (Nom et Email requis).", Toast.LENGTH_SHORT).show()
            }
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

        // Note: Le champ Contact Number n'a pas été rendu obligatoire ici.

        return isValid
    }

    /**
     * Lance l'activité pour la deuxième étape de l'inscription.
     */
    private fun navigateToNextStep() {
        val intent = Intent(this, SignupAssociation2Activity::class.java)
        startActivity(intent)
    }
}
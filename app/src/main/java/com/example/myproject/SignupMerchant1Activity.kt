package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns
import android.widget.TextView // <-- Import nécessaire pour le lien de connexion

class SignupMerchant1Activity : AppCompatActivity() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputContact: EditText
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    // Ajout des vues pour les liens de navigation croisée
    private lateinit var individualButton: Button
    private lateinit var associationButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_merchant1)

        // 1. Initialisation des vues
        inputFullName = findViewById(R.id.input_full_name)
        inputEmail = findViewById(R.id.input_email)
        inputContact = findViewById(R.id.input_contact)
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_previous)

        // Initialisation des nouveaux boutons de navigation croisée (Assumés présents dans le layout)
        individualButton = findViewById(R.id.btn_individual)
        associationButton = findViewById(R.id.btn_association)
        //loginLink = findViewById(R.id.text_login_link)


        // --- LOGIQUE DE NAVIGATION CROISÉE (Ajoutée) ---

        // Clic sur le lien 'Login'
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux d'inscription
        }

        // Clic sur le bouton 'Individual' (Redirige vers le début du flux Individual)
        individualButton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux d'inscription
        }

        // Clic sur le bouton 'Association' (Redirige vers le début du flux Association)
        associationButton.setOnClickListener {
            val intent = Intent(this, SignupAssociation1Activity::class.java)
            startActivity(intent)
            finishAffinity() // Ferme toutes les activités du flux d'inscription
        }


        // 2. --- Logique pour le bouton "Next" avec validation (Existant) ---
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

        // 3. --- Logique pour le bouton "Previous" (Existant) ---
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

        return isValid
    }
}
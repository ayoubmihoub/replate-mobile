package com.example.myproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText // ⬅️ IMPORT AJOUTÉ : Importation de la classe standard EditText
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.textfield.TextInputEditText

/**
 * Cette activité est l'étape finale de l'inscription.
 * Elle collecte les dernières données (ex: mot de passe) et initie l'appel API.
 */
class Activity6 : AppCompatActivity() {

    // Utilisation du type standard EditText pour correspondre au layout XML
    private lateinit var inputPassword: EditText // ⬅️ TYPE CORRIGÉ
    private lateinit var inputConfirmPassword: EditText // ⬅️ TYPE CORRIGÉ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6) // Assurez-vous que R.layout.activity_6 existe

        // --- 1. RÉCUPÉRATION DES DONNÉES ET VÉRIFICATION CRITIQUE ---
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val contact = intent.getStringExtra("contact")
        val location = intent.getStringExtra("location")
        val profileImageUri = intent.getStringExtra("profileImageUri")

        // VÉRIFICATION CRITIQUE : Si les données essentielles manquent, on termine Activity6.
        if (fullName == null || email == null || contact == null) {
            Toast.makeText(this, "Erreur de données (Activity4 non complétée). Retour...", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Initialisation des Vues avec les ID CORRIGÉS
        // Le layout utilise des EditTexts, nous les récupérons directement.
        inputPassword = findViewById(R.id.input_password) // ⬅️ CAST SUPPRIMÉ
        inputConfirmPassword = findViewById(R.id.input_confirm_password) // ⬅️ CAST SUPPRIMÉ

        // CORRECTION des IDs des boutons de navigation
        val completeButton: Button = findViewById(R.id.btn_create_account)
        val prevButton: Button = findViewById(R.id.btn_previous)


        // 3. Logique du bouton Précédent
        prevButton.setOnClickListener {
            finish() // Retourne à Activity5
        }

        // 4. Logique du bouton Finaliser
        completeButton.setOnClickListener {
            // Nous transmettons ici les données essentielles qui devraient être utilisées pour l'inscription API
            if (validateInputs(email, fullName, location, profileImageUri)) {
                // TODO: Appeler le ViewModel pour l'upload et l'inscription
                Toast.makeText(this, "Tentative d'inscription...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(email: String, fullName: String, location: String?, profileImageUri: String?): Boolean {
        // Validation des mots de passe
        val password = inputPassword.text.toString().trim()
        val confirmPassword = inputConfirmPassword.text.toString().trim()

        // NOTE : Il manque la gestion de la CheckBox des termes de service (ID: checkbox_terms) ici.
        // Si elle est obligatoire, vous devriez ajouter une vérification.

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer et confirmer le mot de passe.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            inputConfirmPassword.error = "Les mots de passe ne correspondent pas."
            return false
        }
        if (password.length < 6) {
            inputPassword.error = "Le mot de passe doit avoir au moins 6 caractères."
            return false
        }

        return true
    }
}
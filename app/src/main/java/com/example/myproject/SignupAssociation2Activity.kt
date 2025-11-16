package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View

class SignupAssociation2Activity : AppCompatActivity() {

    // Déclaration des champs de saisie de cette page
    private lateinit var inputLocation: EditText
    private lateinit var inputProfilePicture: EditText
    private lateinit var inputVerificationDocument: EditText
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    // --- LANCEURS D'ACTIVITÉS POUR LA SÉLECTION DE FICHIERS ---

    // Lanceur pour la sélection de l'image de profil ("image/*")
    private val selectPictureLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri)
            inputProfilePicture.setText(fileName)
            Toast.makeText(this, "Photo sélectionnée: $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Aucune photo sélectionnée.", Toast.LENGTH_SHORT).show()
        }
    }

    // Lanceur pour la sélection du document de vérification (documents variés, "*/*")
    private val selectDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri)
            inputVerificationDocument.setText(fileName)
            Toast.makeText(this, "Document sélectionné: $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Aucun document sélectionné.", Toast.LENGTH_SHORT).show()
        }
    }
    // ------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_association2) // Charge le layout de l'étape 2

        // 1. Initialisation des vues
        inputLocation = findViewById(R.id.input_location)
        inputProfilePicture = findViewById(R.id.input_profile_picture)
        inputVerificationDocument = findViewById(R.id.input_verification_document)
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_previous)

        // 2. --- GESTION DU CLIC POUR L'UPLOAD DES FICHIERS ---

        // Clic sur le champ Photo de Profil (La fonctionnalité d'upload reste)
        inputProfilePicture.setOnClickListener {
            selectPictureLauncher.launch("image/*")
        }

        // Clic sur le champ Document de Vérification
        inputVerificationDocument.setOnClickListener {
            selectDocumentLauncher.launch("*/*")
        }

        // 3. --- GESTION DU CLIC "NEXT" ---
        nextButton.setOnClickListener {
            if (validateInputs()) {
                // Si la validation réussit, naviguer vers l'étape 3
                navigateToNextStep()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs requis.", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. --- GESTION DU CLIC "PREVIOUS" ---
        previousButton.setOnClickListener {
            // Revenir à SignupAssociation1Activity
            finish()
        }
    }

    /**
     * Valide que les champs nécessaires pour l'étape 2 sont remplis.
     * La photo de profil est maintenant considérée comme non requise.
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        // 1. Validation de l'Adresse/Location
        if (inputLocation.text.toString().trim().isEmpty()) {
            inputLocation.error = "L'adresse est requise."
            isValid = false
        } else {
            inputLocation.error = null
        }

        // 2. Validation de la Photo de Profil (SUPPRIMÉE pour la rendre NON requise)
        /*
        if (inputProfilePicture.text.toString().trim().isEmpty()) {
            inputProfilePicture.error = "La photo de profil est requise."
            isValid = false
        } else {
            inputProfilePicture.error = null
        }
        */

        // 3. Validation du Document de Vérification (RESTE OBLIGATOIRE)
        if (inputVerificationDocument.text.toString().trim().isEmpty()) {
            inputVerificationDocument.error = "Le document de vérification est requis."
            isValid = false
        } else {
            inputVerificationDocument.error = null
        }

        return isValid
    }

    /**
     * Lance l'activité pour la troisième et dernière étape de l'inscription (SignupAssociation3Activity).
     */
    private fun navigateToNextStep() {
        val intent = Intent(this, SignupAssociation3Activity::class.java)
        startActivity(intent)
    }

    /**
     * Aide à extraire le nom du fichier à partir de l'URI pour l'affichage dans l'EditText.
     */
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        // Si la lecture via ContentResolver échoue, utiliser le chemin (moins fiable)
        return result ?: uri.path?.substringAfterLast('/') ?: "Fichier sélectionné"
    }
}
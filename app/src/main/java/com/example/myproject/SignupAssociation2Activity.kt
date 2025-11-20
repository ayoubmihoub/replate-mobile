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

    // Variables pour stocker les URIs sélectionnées
    private var profilePictureUri: Uri? = null
    private var verificationDocumentUri: Uri? = null
    private lateinit var incomingIntent: Intent

    // Déclaration des champs de saisie de cette page
    private lateinit var inputLocation: EditText
    private lateinit var inputProfilePicture: EditText
    private lateinit var inputVerificationDocument: EditText
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    // NOUVELLES DÉCLARATIONS POUR LA NAVIGATION CROISÉE
    private lateinit var btnIndividual: Button
    private lateinit var btnMerchant: Button

    // --- LANCEURS D'ACTIVITÉS POUR LA SÉLECTION DE FICHIERS ---

    // Lanceur pour la sélection de l'image de profil ("image/*")
    private val selectPictureLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profilePictureUri = uri // Stockage de l'URI
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
            verificationDocumentUri = uri // Stockage de l'URI
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

        // Récupérer les données de l'activité précédente
        incomingIntent = intent

        // 1. Initialisation des vues
        inputLocation = findViewById(R.id.input_location)
        inputProfilePicture = findViewById(R.id.input_profile_picture)
        inputVerificationDocument = findViewById(R.id.input_verification_document)
        nextButton = findViewById(R.id.btn_next)
        previousButton = findViewById(R.id.btn_previous)

        // Initialisation des boutons de navigation croisée
        btnIndividual = findViewById(R.id.btn_individual)
        btnMerchant = findViewById(R.id.btn_merchant)

        // --- GESTION DE LA NAVIGATION CROISÉE ---

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

        // 2. --- GESTION DU CLIC POUR L'UPLOAD DES FICHIERS ---
        inputProfilePicture.setOnClickListener {
            selectPictureLauncher.launch("image/*")
        }
        inputVerificationDocument.setOnClickListener {
            selectDocumentLauncher.launch("*/*")
        }

        // 3. --- GESTION DU CLIC "NEXT" ---
        nextButton.setOnClickListener {
            if (validateInputs()) {
                // Si la validation réussit, naviguer vers l'étape 3 en passant TOUTES les données
                navigateToNextStep()
            } else {
                Toast.makeText(this, "Veuillez remplir le champ d'adresse et fournir le document de vérification.", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. --- GESTION DU CLIC "PREVIOUS" ---
        previousButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Valide que les champs nécessaires pour l'étape 2 sont remplis.
     * La photo de profil est non requise. L'adresse et le document sont requis.
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        // 1. Validation de l'Adresse/Location (Requis)
        if (inputLocation.text.toString().trim().isEmpty()) {
            inputLocation.error = "L'adresse est requise."
            isValid = false
        } else {
            inputLocation.error = null
        }

        // 2. Validation du Document de Vérification (Requis pour une Association)
        if (verificationDocumentUri == null) {
            inputVerificationDocument.error = "Le document de vérification est requis."
            isValid = false
        } else {
            inputVerificationDocument.error = null
        }
        // Note: inputProfilePicture est facultatif

        return isValid
    }

    /**
     * Lance l'activité pour la troisième et dernière étape de l'inscription (SignupAssociation3Activity).
     */
    private fun navigateToNextStep() {
        val intent = Intent(this, SignupAssociation3Activity::class.java).apply {
            // Données de l'activité 1
            putExtra("fullName", incomingIntent.getStringExtra("fullName"))
            putExtra("email", incomingIntent.getStringExtra("email"))
            putExtra("contact", incomingIntent.getStringExtra("contact"))

            // Nouvelles données de l'activité 2
            putExtra("location", inputLocation.text.toString().trim())
            putExtra("profilePictureUri", profilePictureUri?.toString()) // Peut être null
            putExtra("documentUri", verificationDocumentUri?.toString()) // Requis ici, mais passé comme string
        }
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
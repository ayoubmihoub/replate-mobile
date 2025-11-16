package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast

class SignupMerchant2Activity : AppCompatActivity() { // Assurez-vous que le nom de classe est correct

    // Lanceur pour la sélection de l'image de profil
    private val selectPictureLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri)
            findViewById<EditText>(R.id.input_profile_picture).setText(fileName)
            Toast.makeText(this, "Photo sélectionnée: $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Aucune photo sélectionnée.", Toast.LENGTH_SHORT).show()
        }
    }

    // Lanceur pour la sélection du document de vérification
    private val selectDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = getFileName(uri)
            findViewById<EditText>(R.id.input_verification_document).setText(fileName)
            Toast.makeText(this, "Document sélectionné: $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Aucun document sélectionné.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_merchant2) // Assurez-vous que le nom du layout est correct

        // 1. Définir le gestionnaire de clic pour le champ PHOTO DE PROFIL
        val pictureInput: EditText = findViewById(R.id.input_profile_picture)
        pictureInput.setOnClickListener {
            // Lancer le sélecteur d'images (limité aux types d'images)
            selectPictureLauncher.launch("image/*")
        }

        // 2. Définir le gestionnaire de clic pour le champ DOCUMENT DE VÉRIFICATION
        val documentInput: EditText = findViewById(R.id.input_verification_document)
        documentInput.setOnClickListener {
            // Lancer le sélecteur de documents (tous types de fichiers)
            selectDocumentLauncher.launch("*/*")
        }

        // 3. Logique de navigation (inchangée)
        val nextButton: Button = findViewById(R.id.btn_next)
        nextButton.setOnClickListener {
            val intent = Intent(this, SignupMerchant3Activity::class.java)
            startActivity(intent)
        }

        val previousButton: Button = findViewById(R.id.btn_previous)
        previousButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Aide à extraire le nom du fichier à partir de l'URI.
     */
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            // Utilisation de ContentResolver pour lire les métadonnées
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
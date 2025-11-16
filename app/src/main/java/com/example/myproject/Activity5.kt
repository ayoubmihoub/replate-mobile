package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.provider.OpenableColumns
import android.view.MotionEvent
import android.view.View

class Activity5 : AppCompatActivity() {

    private lateinit var inputProfilePicture: EditText
    private lateinit var inputLocation: EditText

    // --- LANCEUR D'ACTIVITÉ POUR LA SÉLECTION D'IMAGE ---
    private val selectPictureLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Ce bloc est exécuté lorsque l'utilisateur sélectionne un fichier
        if (uri != null) {
            val fileName = getFileName(uri)
            inputProfilePicture.setText(fileName)
            Toast.makeText(this, "Photo sélectionnée : $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Aucune photo sélectionnée.", Toast.LENGTH_SHORT).show()
        }
    }
    // ---------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        // 1. Trouver les vues
        val nextButton: Button = findViewById(R.id.btn_next_form)
        val prevButton: Button = findViewById(R.id.btn_previous)
        inputProfilePicture = findViewById(R.id.input_profile_picture)
        inputLocation = findViewById(R.id.input_location)

        // 2. --- GESTION DU CLIC POUR L'UPLOAD DE LA PHOTO ---
        inputProfilePicture.setOnClickListener {
            // Lancer le sélecteur d'images (limité aux types d'images)
            selectPictureLauncher.launch("image/*")
        }

        // --- GESTION DU CLIC POUR L'ICÔNE DE LOCATION ---
        inputLocation.setOnTouchListener { v, event ->
            // Le DrawableEnd (drawableRight) est à l'index 2
            val drawableEnd = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (inputLocation.right - inputLocation.compoundDrawables[drawableEnd].bounds.width())) {
                    openMapSelector()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        // --- Logique du bouton "Next" ---
        nextButton.setOnClickListener {
            if (validateInputs()) {

                // 1. Récupérer l'Intent d'entrée (celle qui a lancé Activity5)
                val incomingIntent = this.intent

                val intent = Intent(this, Activity6::class.java).apply {
                    // 2. Transférer les données de l'Intent d'entrée vers la nouvelle Intent de sortie

                    // Données de Activity4
                    putExtra("fullName", incomingIntent.getStringExtra("fullName"))
                    putExtra("email", incomingIntent.getStringExtra("email"))
                    putExtra("contact", incomingIntent.getStringExtra("contact"))

                    // Données de Activity5
                    putExtra("location", inputLocation.text.toString())
                    // NOTE: Vous devrez gérer l'URI du fichier plutôt que de mettre une URL en dur ici
                    putExtra("profileImageUrl", "http://example.com/profile.jpg")
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Veuillez remplir les champs obligatoires.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Logique du bouton "Previous" ---
        prevButton.setOnClickListener {
            finish()
        }
    }

    // 🚨 MODIFICATION ICI : Suppression de la vérification de la photo de profil 🚨
    /**
     * Valide les champs nécessaires (uniquement Location maintenant).
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        // Vérification de la localisation (RESTE OBLIGATOIRE)
        if (inputLocation.text.toString().trim().isEmpty()) {
            inputLocation.error = "La localisation est requise."
            isValid = false
        } else {
            inputLocation.error = null
        }

        // Ancienne vérification de la photo de profil supprimée pour la rendre non requise
        // if (inputProfilePicture.text.toString().trim().isEmpty()) { ... }

        return isValid
    }

    /**
     * Lance une application de cartographie pour la sélection d'emplacement.
     */
    private fun openMapSelector() {
        val mapUri = Uri.parse("geo:0,0?q=Address")
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Aucune application de carte installée.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Aide à extraire le nom du fichier à partir de l'URI.
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
        return result ?: uri.path?.substringAfterLast('/') ?: "Fichier sélectionné"
    }
}
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

    class SignupMerchant2Activity : AppCompatActivity() {

        private var profilePictureUri: Uri? = null
        private var verificationDocumentUri: Uri? = null
        private lateinit var incomingIntent: Intent

        // Lanceur pour la sélection de l'image de profil
        private val selectPictureLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                profilePictureUri = uri
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
                verificationDocumentUri = uri
                val fileName = getFileName(uri)
                findViewById<EditText>(R.id.input_verification_document).setText(fileName)
                Toast.makeText(this, "Document sélectionné: $fileName", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Aucun document sélectionné.", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.signup_merchant2)

            // Récupérer les données de l'activité précédente
            incomingIntent = intent

            // 1. Définir le gestionnaire de clic pour le champ PHOTO DE PROFIL
            val pictureInput: EditText = findViewById(R.id.input_profile_picture)
            pictureInput.setOnClickListener {
                selectPictureLauncher.launch("image/*")
            }

            // 2. Définir le gestionnaire de clic pour le champ DOCUMENT DE VÉRIFICATION
            val documentInput: EditText = findViewById(R.id.input_verification_document)
            documentInput.setOnClickListener {
                selectDocumentLauncher.launch("*/*")
            }

            // 3. Logique de navigation (MODIFIÉ : document non requis)
            val nextButton: Button = findViewById(R.id.btn_next)
            nextButton.setOnClickListener {
                // ✅ SUPPRIMÉ la vérification obligatoire du document
                // if (verificationDocumentUri == null) {
                //     Toast.makeText(this, "Veuillez sélectionner le document de vérification.", Toast.LENGTH_SHORT).show()
                //     return@setOnClickListener
                // }

                // On utilise l'URI comme une chaîne pour le passage, en attendant un vrai upload.
                val documentUriString = verificationDocumentUri?.toString()
                val pictureUriString = profilePictureUri?.toString()

                val intent = Intent(this, SignupMerchant3Activity::class.java).apply {
                    // Données de l'activité 1
                    putExtra("fullName", incomingIntent.getStringExtra("fullName"))
                    putExtra("email", incomingIntent.getStringExtra("email"))
                    putExtra("contact", incomingIntent.getStringExtra("contact"))

                    // Nouvelles données de l'activité 2
                    putExtra("documentUri", documentUriString)
                    putExtra("profilePictureUri", pictureUriString)
                }
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
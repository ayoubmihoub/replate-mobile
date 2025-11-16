    package com.example.myproject



    import android.content.Intent
    import android.os.Bundle
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.ViewModelProvider
    import com.example.myproject.data.model.UserRole
    import com.example.myproject.ui.register.RegisterViewModel

    /**
     * Activité finale pour l'inscription d'un Marchand.
     * Elle collecte le mot de passe et déclenche l'appel API via le ViewModel avec UserRole.MERCHANT.
     */
    class FinSignupMerchantActivity : AppCompatActivity() {

        private lateinit var registerViewModel: RegisterViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Note: Nous n'avons pas besoin d'un layout complexe ici, le travail se fait en arrière-plan
            // ou vous pouvez utiliser le layout d'Activity6 si les IDs sont les mêmes.
            setContentView(R.layout.activity_6) // Réutilisation du layout précédent pour l'affichage de base.

            // 1. Initialisation du ViewModel
            registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

            // --- Récupération de TOUTES les données passées via Intent ---
            val incomingIntent = intent
            val fullName = incomingIntent.getStringExtra("fullName")
            val email = incomingIntent.getStringExtra("email")
            val contact = incomingIntent.getStringExtra("contact") // phoneNumber
            val password = incomingIntent.getStringExtra("password")

            // Données spécifiques au marchand
            val documentUri = incomingIntent.getStringExtra("documentUri")
            // val profilePictureUri = incomingIntent.getStringExtra("profilePictureUri") // Non utilisé dans l'API RegisterRequest

            // VÉRIFICATION CRITIQUE : Les données essentielles doivent être présentes.
            if (fullName.isNullOrBlank() || email.isNullOrBlank() || contact.isNullOrBlank() || password.isNullOrBlank() || documentUri.isNullOrBlank()) {
                Toast.makeText(this, "Erreur de données requises pour le Marchand. Redirection.", Toast.LENGTH_LONG).show()
                // Simuler un retour au début ou simplement fermer
                finishAffinity()
                return
            }

            // Simuler une URL publique pour le document, en attendant un vrai upload.
            // C'est cette URL que l'API attend, pas l'URI locale.
            val documentUrl = "http://mockapi.com/documents/$fullName-${System.currentTimeMillis()}.pdf"


            // 2. Observation du statut d'inscription
            registerViewModel.registrationStatus.observe(this) { status ->
                Toast.makeText(this, status, Toast.LENGTH_LONG).show()

                // Pour le Marchand, le statut de succès pourrait être "Compte en attente de validation."
                if (status.contains("Inscription réussie")) {
                    // Naviguer vers un écran de statut ou de connexion
                    // val intent = Intent(this, StatusScreenActivity::class.java)
                    // startActivity(intent)
                    // finishAffinity()
                }
            }

            // Gère la réponse de succès de l'API
            registerViewModel.messageResponse.observe(this) { response ->
                if (response != null) {
                    // L'inscription a réussi. Naviguer vers l'écran de connexion/accueil.
                    Toast.makeText(this, "Compte Marchand créé! En attente de validation Admin.", Toast.LENGTH_LONG).show()
                    // Exemple de navigation:
                    // val intent = Intent(this, LoginActivity::class.java)
                    // startActivity(intent)
                    // finishAffinity() // Ferme toutes les activités du flux d'inscription
                }
            }

            // 3. Déclenche l'appel API immédiatement (puisque c'est l'activité finale)
            registerMerchant(
                email = email,
                password = password,
                username = fullName,
                phoneNumber = contact,
                documentUrl = documentUrl
            )
        }

        /**
         * Déclenche l'appel API d'inscription avec le rôle MERCHANT.
         */
        private fun registerMerchant(
            email: String,
            password: String,
            username: String,
            phoneNumber: String,
            documentUrl: String
        ) {
            // NOTE: On suppose que la location n'est pas collectée dans ce flux, on la laisse à null.
            this.registerViewModel.registerUser(
                email = email,
                password = password,
                role = UserRole.MERCHANT, // ⬅️ Rôle de Marchand
                username = username,
                location = null,
                phoneNumber = phoneNumber,
                documentUrl = documentUrl // ⬅️ URL du document requis pour le Marchand
            )
        }
    }
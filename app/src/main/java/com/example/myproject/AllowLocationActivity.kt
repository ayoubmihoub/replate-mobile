package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import com.example.myproject.data.model.UserRole // N'oubliez pas cet import !

class AllowLocationActivity : AppCompatActivity() {

    // Déclaration du rôle comme propriété pour être accessible dans l'OnClickListener
    private lateinit var userRole: UserRole

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allow_location)

        // 1. Récupérer le rôle passé par LoginActivity
        val targetRoleName = intent.getStringExtra("TARGET_ROLE")
        userRole = try {
            // S'assurer que le rôle est récupéré ou utiliser INDIVIDUAL par défaut
            UserRole.valueOf(targetRoleName ?: UserRole.INDIVIDUAL.name)
        } catch (e: IllegalArgumentException) {
            UserRole.INDIVIDUAL
        }

        // 2. Trouver le bouton "Access Location"
        val accessLocationButton: Button = findViewById(R.id.button_access_location)

        // --- GESTION DU CLIC "ACCESS LOCATION" ---
        accessLocationButton.setOnClickListener {
            // Lancer la redirection conditionnelle
            handleFinalRedirection(userRole)
        }
    }

    /**
     * Gère la navigation finale vers l'écran d'accueil approprié en fonction du rôle.
     */
    private fun handleFinalRedirection(role: UserRole) {
        val targetActivity = when (role) {
            // INDIVIDUAL -> Home2Activity
            UserRole.INDIVIDUAL -> Home2Activity::class.java

            // ASSOCIATION ou MERCHANT -> HomePage
            UserRole.ASSOCIATION, UserRole.MERCHANT -> HomePage::class.java

            // Fallback (devrait être déjà filtré par LoginActivity)
            else -> HomePage::class.java
        }

        val intent = Intent(this, targetActivity).apply {
            // Flags cruciaux pour effacer la pile
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Lancer l'activité d'accueil
        startActivity(intent)
        finish() // Fermer AllowLocationActivity
    }
}
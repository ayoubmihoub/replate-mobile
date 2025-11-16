package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class AllowLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Utilise le layout allow_location.xml
        setContentView(R.layout.allow_location)

        // 1. Trouver le bouton "Access Location"
        // L'ID du bouton dans allow_location.xml est 'button_access_location'
        val accessLocationButton: Button = findViewById(R.id.button_access_location)

        // --- GESTION DU CLIC "ACCESS LOCATION" ---
        accessLocationButton.setOnClickListener {

            // Créer un Intent pour démarrer la HomeActivity
            // HomeActivity correspond à home_page.xml
            val intent = Intent(this, HomePage::class.java)

            // Flags cruciaux : Ceci efface toutes les activités précédentes (Login, Signup, etc.)
            // de la pile. L'utilisateur ne pourra pas revenir à AllowLocationActivity ou LoginActivity.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Lancer l'activité d'accueil
            startActivity(intent)
        }
    }
}
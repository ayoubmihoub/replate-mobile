package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent // Import de l'Intent
import android.widget.Button   // Import du Button

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        // 1. Trouver le bouton par son ID "button_next" (défini dans activity_2.xml)
        val nextButton: Button = findViewById(R.id.button_next)

        // 2. Définir l'action de clic
        nextButton.setOnClickListener {
            // 3. Créer un Intent pour démarrer la classe Activity3
            val intent = Intent(this, Activity3::class.java)

            // Lancer la nouvelle Activité
            startActivity(intent)
        }
    }
}
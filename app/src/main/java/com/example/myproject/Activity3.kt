package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent // N'oubliez pas l'importation de l'Intent
import android.widget.Button   // N'oubliez pas l'importation du Button

class Activity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        // 1. Trouver le bouton par son ID "button_next" (défini dans activity_3.xml)
        val nextButton: Button = findViewById(R.id.button_next)

        // 2. Définir l'action de clic
        nextButton.setOnClickListener {
            // 3. Créer un Intent pour démarrer la classe Activity4
            val intent = Intent(this, Activity4::class.java)

            // Lancer la nouvelle Activité
            startActivity(intent)
        }
    }
}
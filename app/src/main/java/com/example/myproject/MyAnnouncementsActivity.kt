package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MyAnnouncementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_announcments)


        // Trouver le bouton pour créer une nouvelle annonce
        val newAnnouncementButton = findViewById<CardView>(R.id.btn_new_announcement)

        // Définir l'action pour la nouvelle annonce
        newAnnouncementButton.setOnClickListener {
            val intent = Intent(this, NewAnnouncementActivity::class.java)
            startActivity(intent)
        }

        // --- LOGIQUE AJOUTÉE : ACTIVATION DU BOUTON RETOUR ---

        // 1. Trouver le bouton 'Back' (btn_back)
        val backButton = findViewById<ImageButton>(R.id.btn_back)

        // 2. Définir l'action au clic : fermer cette activité
        backButton.setOnClickListener {
            finish()
        }
    }

}